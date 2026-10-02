package com.example.propinas.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.propinas.R
import com.example.propinas.model.CalidadServicio
import kotlin.math.roundToInt

/**
 * Componente STATELESS para seleccionar el porcentaje de propina mediante un Slider
 * y FilterChips de calidad del servicio.
 */
@Composable
fun SelectorPropina(
    porcentajePropina: Int,
    calidadServicio: CalidadServicio?,
    onPropinaChange: (Int) -> Unit,
    onServicioSelected: (CalidadServicio) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Título con el porcentaje actual
        Text(
            text = stringResource(id = R.string.propina_porcentaje_label, porcentajePropina),
            style = MaterialTheme.typography.titleMedium
        )

        // Slider para selección fina (0% a 30%)
        Slider(
            value = porcentajePropina.toFloat(),
            onValueChange = { onPropinaChange(it.roundToInt()) },
            valueRange = 0f..30f,
            steps = 29,
            modifier = Modifier.fillMaxWidth()
        )

        // Etiqueta para las opciones predefinidas
        Text(
            text = stringResource(id = R.string.calidad_servicio_label),
            style = MaterialTheme.typography.bodyMedium
        )

        // Chips de calidad del servicio con scroll horizontal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalidadServicio.entries.forEach { servicio ->
                val textoChip = when (servicio) {
                    CalidadServicio.MALO -> stringResource(id = R.string.servicio_malo)
                    CalidadServicio.REGULAR -> stringResource(id = R.string.servicio_regular)
                    CalidadServicio.BUENO -> stringResource(id = R.string.servicio_bueno)
                    CalidadServicio.EXCELENTE -> stringResource(id = R.string.servicio_excelente)
                }

                FilterChip(
                    selected = calidadServicio == servicio,
                    onClick = { onServicioSelected(servicio) },
                    label = { Text(text = textoChip) }
                )
            }
        }
    }
}
