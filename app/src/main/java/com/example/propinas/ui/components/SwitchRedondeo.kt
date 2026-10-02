package com.example.propinas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propinas.R

/**
 * Componente STATELESS con un Switch para redondear el total hacia arriba.
 */
@Composable
fun SwitchRedondeo(
    redondearTotal: Boolean,
    onRedondearChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.redondear_label),
            style = MaterialTheme.typography.bodyLarge
        )

        Switch(
            checked = redondearTotal,
            onCheckedChange = onRedondearChange
        )
    }
}
