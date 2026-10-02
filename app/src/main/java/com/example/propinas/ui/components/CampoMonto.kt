package com.example.propinas.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.propinas.R

/**
 * Componente STATELESS para ingresar el monto de la cuenta.
 * No mantiene estado interno (sin remember ni mutableStateOf).
 */
@Composable
fun CampoMonto(
    montoInput: String,
    esMontoInvalido: Boolean,
    onMontoChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = montoInput,
        onValueChange = onMontoChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.monto_label)) },
        prefix = { Text(text = stringResource(id = R.string.monto_prefijo)) },
        placeholder = { Text(text = stringResource(id = R.string.monto_placeholder)) },
        singleLine = true,
        isError = esMontoInvalido,
        supportingText = {
            if (esMontoInvalido) {
                Text(
                    text = stringResource(id = R.string.monto_error_invalido),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        )
    )
}
