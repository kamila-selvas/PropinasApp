package com.example.propinas.model

/**
 * Enum que clasifica la propina según su porcentaje para decidir el color en la UI.
 * El ViewModel determina el nivel y la UI lo mapea al color correspondiente.
 */
enum class NivelPropina {
    BAJO,   // < 10%  -> Rojo
    MEDIO,  // 10% - 14% -> Ámbar
    ALTO    // >= 15% -> Verde
}

/**
 * Representa la cuota a pagar por cada persona en la lista de desglose.
 */
data class PersonaDesglose(
    val numeroPersona: Int,
    val montoTexto: String,
    val tieneAjusteRedondeo: Boolean = false
)

/**
 * Estado inmutable de la UI (StateFlow / UDF).
 * Contiene todas las entradas del usuario y los resultados pre-formateados para mostrar.
 */
data class PropinaUiState(
    // Entradas del usuario
    val montoInput: String = "",
    val porcentajePropina: Int = 15,
    val calidadServicio: CalidadServicio? = CalidadServicio.BUENO,
    val numPersonas: Int = 1,
    val redondearTotal: Boolean = false,

    // Estados de validación y cálculo
    val esMontoInvalido: Boolean = false,
    val esCalculado: Boolean = false,

    // Resultados calculados y formateados para la UI "tonta"
    val subtotalTexto: String = "$0.00",
    val montoPropinaTexto: String = "$0.00",
    val totalTexto: String = "$0.00",
    val montoPorPersonaTexto: String = "$0.00",
    val progresoPropina: Float = 0f,
    val nivelPropina: NivelPropina = NivelPropina.ALTO,
    val desglosePersonas: List<PersonaDesglose> = emptyList()
)
