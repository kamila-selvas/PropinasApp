package com.example.propinas

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

data class PropinaUiState(
    val montoInput: String = "",
    val porcentajePropina: Int = 15,
    val numPersonas: Int = 1,
    val esMontoInvalido: Boolean = false,
    val subtotalTexto: String = "$0.00",
    val montoPropinaTexto: String = "$0.00",
    val totalTexto: String = "$0.00",
    val montoPorPersonaTexto: String = "$0.00",
    val nombreEstudiante: String = "",
    val matriculaEstudiante: String = ""
)

class PropinaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PropinaUiState())
    val uiState: StateFlow<PropinaUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { recalcularEstado(it) }
    }

    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombreEstudiante = nuevoNombre) }
    }

    fun onMatriculaChange(nuevaMatricula: String) {
        _uiState.update { it.copy(matriculaEstudiante = nuevaMatricula) }
    }

    fun onMontoChange(nuevoMonto: String) {
        val montoLimpio = nuevoMonto.filter { it.isDigit() || it == '.' || it == ',' }
            .replace(',', '.')
        if (montoLimpio.count { it == '.' } > 1) return

        _uiState.update { estado ->
            recalcularEstado(estado.copy(montoInput = montoLimpio))
        }
    }

    fun onPropinaChange(nuevoPorcentaje: Int) {
        _uiState.update { estado ->
            recalcularEstado(estado.copy(porcentajePropina = nuevoPorcentaje.coerceIn(0, 30)))
        }
    }

    fun onPersonasChange(nuevasPersonas: Int) {
        _uiState.update { estado ->
            recalcularEstado(estado.copy(numPersonas = nuevasPersonas.coerceIn(1, 20)))
        }
    }

    private fun recalcularEstado(estado: PropinaUiState): PropinaUiState {
        val monto = estado.montoInput.toDoubleOrNull()

        if (monto == null || monto <= 0.0) {
            return estado.copy(
                esMontoInvalido = estado.montoInput.isNotEmpty(),
                subtotalTexto = "$0.00",
                montoPropinaTexto = "$0.00",
                totalTexto = "$0.00",
                montoPorPersonaTexto = "$0.00"
            )
        }

        val subtotal = monto
        val montoPropina = subtotal * (estado.porcentajePropina / 100.0)
        val total = subtotal + montoPropina
        val montoPorPersona = total / estado.numPersonas

        return estado.copy(
            esMontoInvalido = false,
            subtotalTexto = formatearMoneda(subtotal),
            montoPropinaTexto = formatearMoneda(montoPropina),
            totalTexto = formatearMoneda(total),
            montoPorPersonaTexto = formatearMoneda(montoPorPersona)
        )
    }

    private fun formatearMoneda(valor: Double): String {
        return "$%.2f".format(Locale.US, valor)
    }
}
