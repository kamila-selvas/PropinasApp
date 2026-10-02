package com.example.propinas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.propinas.R
import com.example.propinas.model.NivelPropina
import com.example.propinas.ui.theme.PropinaNivelAmbar
import com.example.propinas.ui.theme.PropinaNivelRojo
import com.example.propinas.ui.theme.PropinaNivelVerde

/**
 * Componente STATELESS que muestra la tarjeta de resumen de la cuenta.
 * Incluye el subtotal, propina, total, indicador de progreso y de nivel (color).
 */
@Composable
fun TarjetaResumen(
    subtotalTexto: String,
    montoPropinaTexto: String,
    totalTexto: String,
    progresoPropina: Float,
    nivelPropina: NivelPropina,
    modifier: Modifier = Modifier
) {
    // Determina el color y el texto según el enum NivelPropina decidido por el ViewModel
    val (colorNivel, textoNivel) = when (nivelPropina) {
        NivelPropina.ALTO -> PropinaNivelVerde to stringResource(id = R.string.nivel_propina_alto)
        NivelPropina.MEDIO -> PropinaNivelAmbar to stringResource(id = R.string.nivel_propina_medio)
        NivelPropina.BAJO -> PropinaNivelRojo to stringResource(id = R.string.nivel_propina_bajo)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.resumen_titulo),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Subtotal
            FilaMonto(
                etiqueta = stringResource(id = R.string.subtotal_label),
                valor = subtotalTexto
            )

            // Propina
            FilaMonto(
                etiqueta = stringResource(id = R.string.propina_monto_label),
                valor = montoPropinaTexto
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Total a pagar
            FilaMonto(
                etiqueta = stringResource(id = R.string.total_label),
                valor = totalTexto,
                esDestacado = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // LinearProgressIndicator que muestra qué proporción del total es propina
            LinearProgressIndicator(
                progress = { progresoPropina },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = colorNivel,
            )

            // Indicador visual con círculo de color y texto informativo del nivel
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(colorNivel)
                )
                Text(
                    text = textoNivel,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorNivel,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Composable auxiliar interno para filas de monto.
 */
@Composable
private fun FilaMonto(
    etiqueta: String,
    valor: String,
    esDestacado: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = etiqueta,
            style = if (esDestacado) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (esDestacado) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = valor,
            style = if (esDestacado) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            fontWeight = if (esDestacado) FontWeight.Bold else FontWeight.Medium,
            color = if (esDestacado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
