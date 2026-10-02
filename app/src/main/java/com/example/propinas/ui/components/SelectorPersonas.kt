package com.example.propinas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propinas.R
import kotlin.math.roundToInt

/**
 * Componente STATELESS para seleccionar el número de personas.
 * Incluye botones +/- y un Slider para ajuste rápido.
 */
@Composable
fun SelectorPersonas(
    numPersonas: Int,
    onPersonasChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.personas_label, numPersonas),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botón para decrementar (-)
            FilledIconButton(
                onClick = { onPersonasChange(numPersonas - 1) },
                enabled = numPersonas > 1
            ) {
                Text(text = "-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            // Muestra el número actual en grande
            Text(
                text = "$numPersonas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 48.dp)
            )

            // Botón para incrementar (+)
            FilledIconButton(
                onClick = { onPersonasChange(numPersonas + 1) },
                enabled = numPersonas < 20
            ) {
                Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Slider para selección continua de personas (1 a 20)
        Slider(
            value = numPersonas.toFloat(),
            onValueChange = { onPersonasChange(it.roundToInt()) },
            valueRange = 1f..20f,
            steps = 18,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
