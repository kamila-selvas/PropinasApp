package com.example.propinas.model

/**
 * Enum que representa las opciones predefinidas de calidad del servicio.
 * Cada opción asigna un porcentaje fijo al slider de propina.
 */
enum class CalidadServicio(val porcentaje: Int) {
    MALO(5),
    REGULAR(10),
    BUENO(15),
    EXCELENTE(20)
}
