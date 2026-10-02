package com.example.propinas.viewmodel

import androidx.lifecycle.ViewModel
import com.example.propinas.model.CalidadServicio
import com.example.propinas.model.NivelPropina
import com.example.propinas.model.PersonaDesglose
import com.example.propinas.model.PropinaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * ViewModel que gestiona la lógica de negocio y los cálculos de la calculadora.
 * Mantiene el estado 100% en memoria y se encarga de entregar la información ya formateada.
 */
class PropinaViewModel : ViewModel() {

    // Backing Property: _uiState es privado y mutable, solo modificable dentro del ViewModel
    private val _uiState = MutableStateFlow(PropinaUiState())

    // uiState es público e inmutable, expuesto a la UI como StateFlow
    val uiState: StateFlow<PropinaUiState> = _uiState.asStateFlow()

    init {
        // Inicializar calculando el estado inicial por defecto
        _uiState.update { recalcularEstado(it) }
    }

    // --- EVENTOS UDF (Unidirectional Data Flow) ---

    /**
     * Evento: Cambio en el texto del monto de la cuenta.
     */
    fun onMontoChange(nuevoMonto: String) {
        // Filtrar caracteres válidos para un número decimal (dígitos y un punto/coma)
        val montoLimpio = nuevoMonto.filter { it.isDigit() || it == '.' || it == ',' }
            .replace(',', '.')

        // Permitir solo un punto decimal
        if (montoLimpio.count { it == '.' } > 1) return

        _uiState.update { estadoActual ->
            recalcularEstado(estadoActual.copy(montoInput = montoLimpio))
        }
    }

    /**
     * Evento: Cambio en el porcentaje de propina a través del Slider.
     */
    fun onPropinaChange(nuevoPorcentaje: Int) {
        val porcentajeClampeado = nuevoPorcentaje.coerceIn(0, 30)
        // Verificar si el porcentaje coincide con algún chip predefinido
        val calidadCoincidente = CalidadServicio.entries.find { it.porcentaje == porcentajeClampeado }

        _uiState.update { estadoActual ->
            recalcularEstado(
                estadoActual.copy(
                    porcentajePropina = porcentajeClampeado,
                    calidadServicio = calidadCoincidente
                )
            )
        }
    }

    /**
     * Evento: Selección de un FilterChip de calidad de servicio.
     */
    fun onServicioSelected(servicio: CalidadServicio) {
        _uiState.update { estadoActual ->
            recalcularEstado(
                estadoActual.copy(
                    porcentajePropina = servicio.porcentaje,
                    calidadServicio = servicio
                )
            )
        }
    }

    /**
     * Evento: Cambio en el número de personas (limitado de 1 a 20).
     */
    fun onPersonasChange(nuevasPersonas: Int) {
        val personasValidas = nuevasPersonas.coerceIn(1, 20)
        _uiState.update { estadoActual ->
            recalcularEstado(estadoActual.copy(numPersonas = personasValidas))
        }
    }

    /**
     * Evento: Cambio en el Switch de redondeo del total.
     */
    fun onRedondearChange(redondear: Boolean) {
        _uiState.update { estadoActual ->
            recalcularEstado(estadoActual.copy(redondearTotal = redondear))
        }
    }

    // --- LÓGICA DE NEGOCIO Y CÁLCULOS ---

    /**
     * Recalcula todos los campos derivados del estado inmutable y retorna una copia actualizada.
     */
    private fun recalcularEstado(estado: PropinaUiState): PropinaUiState {
        val monto = estado.montoInput.toDoubleOrNull()

        // Si el monto está vacío o no es un número válido > 0
        if (monto == null || monto <= 0.0) {
            val esInvalido = estado.montoInput.isNotEmpty()
            return estado.copy(
                esMontoInvalido = esInvalido,
                esCalculado = false,
                subtotalTexto = "$0.00",
                montoPropinaTexto = "$0.00",
                totalTexto = "$0.00",
                montoPorPersonaTexto = "$0.00",
                progresoPropina = 0f,
                desglosePersonas = emptyList()
            )
        }

        // Cálculos numéricos
        val subtotal = monto
        val montoPropina = subtotal * (estado.porcentajePropina / 100.0)
        val totalSinRedondear = subtotal + montoPropina
        val totalFinal = if (estado.redondearTotal) ceil(totalSinRedondear) else totalSinRedondear

        val montoPorPersona = totalFinal / estado.numPersonas

        // Progreso de propina relativo al total
        val progreso = if (totalFinal > 0.0) (montoPropina / totalFinal).toFloat().coerceIn(0f, 1f) else 0f

        // Clasificación del nivel de propina
        val nivel = when {
            estado.porcentajePropina >= 15 -> NivelPropina.ALTO
            estado.porcentajePropina in 10..14 -> NivelPropina.MEDIO
            else -> NivelPropina.BAJO
        }

        // Generación del desglose por persona en memoria
        val desglose = generarDesglosePersonas(
            totalFinal = totalFinal,
            numPersonas = estado.numPersonas,
            redondearTotal = estado.redondearTotal,
            montoPorPersonaSinRedondear = montoPorPersona
        )

        return estado.copy(
            esMontoInvalido = false,
            esCalculado = true,
            subtotalTexto = formatearMoneda(subtotal),
            montoPropinaTexto = formatearMoneda(montoPropina),
            totalTexto = formatearMoneda(totalFinal),
            montoPorPersonaTexto = formatearMoneda(montoPorPersona),
            progresoPropina = progreso,
            nivelPropina = nivel,
            desglosePersonas = desglose
        )
    }

    /**
     * Genera la lista de desglose por persona.
     * Si se redondea el total, la última persona absorbe los centavos sobrantes.
     */
    private fun generarDesglosePersonas(
        totalFinal: Double,
        numPersonas: Int,
        redondearTotal: Boolean,
        montoPorPersonaSinRedondear: Double
    ): List<PersonaDesglose> {
        if (numPersonas <= 0) return emptyList()

        if (redondearTotal && numPersonas > 1) {
            val totalCentavos = (totalFinal * 100).roundToInt()
            val baseCentavos = totalCentavos / numPersonas
            val sobranteCentavos = totalCentavos % numPersonas

            return List(numPersonas) { index ->
                val esUltimaPersona = index == numPersonas - 1
                val centavosEstaPersona = if (esUltimaPersona) baseCentavos + sobranteCentavos else baseCentavos
                val montoPersona = centavosEstaPersona / 100.0

                PersonaDesglose(
                    numeroPersona = index + 1,
                    montoTexto = formatearMoneda(montoPersona),
                    tieneAjusteRedondeo = esUltimaPersona && sobranteCentavos != 0
                )
            }
        } else {
            return List(numPersonas) { index ->
                PersonaDesglose(
                    numeroPersona = index + 1,
                    montoTexto = formatearMoneda(montoPorPersonaSinRedondear),
                    tieneAjusteRedondeo = false
                )
            }
        }
    }

    /**
     * Formatea un valor numérico a representación monetaria "$X.XX".
     */
    private fun formatearMoneda(valor: Double): String {
        return "$%.2f".format(Locale.US, valor)
    }
}
