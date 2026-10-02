package com.example.propinas.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.propinas.R
import com.example.propinas.ui.components.CampoMonto
import com.example.propinas.ui.components.ListaDesglose
import com.example.propinas.ui.components.SelectorPersonas
import com.example.propinas.ui.components.SelectorPropina
import com.example.propinas.ui.components.SwitchRedondeo
import com.example.propinas.ui.components.TarjetaPorPersona
import com.example.propinas.ui.components.TarjetaResumen
import com.example.propinas.viewmodel.PropinaViewModel

/**
 * Pantalla principal de la Calculadora de Propinas.
 * Consume el estado inmutable desde el PropinaViewModel respetando UDF (Unidirectional Data Flow).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropinaScreen(
    viewModel: PropinaViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // collectAsStateWithLifecycle recolecta el StateFlow de manera segura con el ciclo de vida de la UI,
    // cancelando la recolección cuando la app pasa a segundo plano para ahorrar recursos.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.titulo_pantalla),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Campo para ingresar el monto de la cuenta
            CampoMonto(
                montoInput = uiState.montoInput,
                esMontoInvalido = uiState.esMontoInvalido,
                onMontoChange = viewModel::onMontoChange
            )

            // 2. Selector de porcentaje de propina (Slider + FilterChips)
            SelectorPropina(
                porcentajePropina = uiState.porcentajePropina,
                calidadServicio = uiState.calidadServicio,
                onPropinaChange = viewModel::onPropinaChange,
                onServicioSelected = viewModel::onServicioSelected
            )

            // 3. Selector del número de personas (+/- y Slider)
            SelectorPersonas(
                numPersonas = uiState.numPersonas,
                onPersonasChange = viewModel::onPersonasChange
            )

            // 4. Switch para redondear el total
            SwitchRedondeo(
                redondearTotal = uiState.redondearTotal,
                onRedondearChange = viewModel::onRedondearChange
            )

            HorizontalDivider()

            // 5. Tarjeta con el resumen general (Subtotal, Propina, Total e Indicador de nivel)
            TarjetaResumen(
                subtotalTexto = uiState.subtotalTexto,
                montoPropinaTexto = uiState.montoPropinaTexto,
                totalTexto = uiState.totalTexto,
                progresoPropina = uiState.progresoPropina,
                nivelPropina = uiState.nivelPropina
            )

            // 6. Tarjeta destacada con la cuota individual por persona
            TarjetaPorPersona(
                montoPorPersonaTexto = uiState.montoPorPersonaTexto,
                numPersonas = uiState.numPersonas
            )

            // 7. Lista con el desglose individual por persona
            ListaDesglose(
                desglosePersonas = uiState.desglosePersonas,
                esCalculado = uiState.esCalculado
            )
        }
    }
}
