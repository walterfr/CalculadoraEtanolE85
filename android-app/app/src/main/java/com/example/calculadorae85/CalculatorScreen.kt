package com.example.calculadorae85

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val currencyFormat = remember { NumberFormat.getCurrencyInstance() }
    val currencySymbol = remember(currencyFormat) { currencyFormat.currency?.symbol ?: "$" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header Card (Configurações Gerais)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.main_settings), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.targetE85,
                        onValueChange = viewModel::onTargetE85Changed,
                        label = { Text(stringResource(R.string.target_blend_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        suffix = { Text(stringResource(R.string.unit_percent)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = state.pctGasoline,
                            onValueChange = viewModel::onPctGasolineChanged,
                            label = { Text(stringResource(R.string.ethanol_in_gasoline_label)) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            suffix = { Text(stringResource(R.string.unit_percent)) }
                        )
                        OutlinedTextField(
                            value = state.pctAlcohol,
                            onValueChange = viewModel::onPctAlcoholChanged,
                            label = { Text(stringResource(R.string.ethanol_in_ethanol_label)) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            suffix = { Text(stringResource(R.string.unit_percent)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            PrimaryTabRow(
                selectedTabIndex = state.activeTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = state.activeTab == CalculatorTab.SIMPLE,
                    onClick = { viewModel.onTabSelected(CalculatorTab.SIMPLE) },
                    text = { Text(stringResource(R.string.tab_fixed_volume)) }
                )
                Tab(
                    selected = state.activeTab == CalculatorTab.FILL,
                    onClick = { viewModel.onTabSelected(CalculatorTab.FILL) },
                    text = { Text(stringResource(R.string.tab_fill)) }
                )
                Tab(
                    selected = state.activeTab == CalculatorTab.MONEY,
                    onClick = { viewModel.onTabSelected(CalculatorTab.MONEY) },
                    text = { Text(stringResource(R.string.tab_money)) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Content
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    when (state.activeTab) {
                        CalculatorTab.SIMPLE -> {
                            OutlinedTextField(
                                value = state.totalVolume,
                                onValueChange = viewModel::onTotalVolumeChanged,
                                label = { Text(stringResource(R.string.total_volume_label)) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                suffix = { Text(stringResource(R.string.unit_liters)) }
                            )
                        }
                        CalculatorTab.FILL -> {
                            OutlinedTextField(
                                value = state.tankCapacity,
                                onValueChange = viewModel::onTankCapacityChanged,
                                label = { Text(stringResource(R.string.tank_capacity_label)) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                suffix = { Text(stringResource(R.string.unit_liters)) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = state.currentVolume,
                                onValueChange = viewModel::onCurrentVolumeChanged,
                                label = { Text(stringResource(R.string.current_fuel_label)) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                suffix = { Text(stringResource(R.string.unit_liters)) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = state.currentEthanolPct,
                                onValueChange = viewModel::onCurrentEthanolPctChanged,
                                label = { Text(stringResource(R.string.current_ethanol_pct_label)) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                suffix = { Text(stringResource(R.string.unit_percent)) }
                            )
                        }
                        CalculatorTab.MONEY -> {
                            OutlinedTextField(
                                value = state.totalMoney,
                                onValueChange = viewModel::onTotalMoneyChanged,
                                label = { Text(stringResource(R.string.total_money_label)) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                prefix = { Text(currencySymbol) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = state.priceGas,
                                    onValueChange = viewModel::onPriceGasChanged,
                                    label = { Text(stringResource(R.string.price_gasoline_label)) },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    prefix = { Text(currencySymbol) }
                                )
                                OutlinedTextField(
                                    value = state.priceAlc,
                                    onValueChange = viewModel::onPriceAlcChanged,
                                    label = { Text(stringResource(R.string.price_ethanol_label)) },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    prefix = { Text(currencySymbol) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Results
            val error = state.error
            if (error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = error.localizedMessage(),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (state.resGasLiters > 0 || state.resAlcLiters > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.results_title),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            // Gas Card
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(stringResource(R.string.gasoline_upper), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                                    Text(
                                        text = stringResource(R.string.liters_format, state.resGasLiters),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    if (state.activeTab == CalculatorTab.MONEY) {
                                        Text(
                                            text = currencyFormat.format(state.resGasMoney),
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    }
                                }
                            }

                            // Alcohol Card
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(stringResource(R.string.ethanol_upper), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                    Text(
                                        text = stringResource(R.string.liters_format, state.resAlcLiters),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    if (state.activeTab == CalculatorTab.MONEY) {
                                        Text(
                                            text = currencyFormat.format(state.resAlcMoney),
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.footer),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CalculatorError.localizedMessage(): String = when (this) {
    is CalculatorError.TargetOutOfRange -> stringResource(
        R.string.error_target_out_of_range,
        target.toDisplayString(),
        pctGasoline.toDisplayString(),
        pctAlcohol.toDisplayString()
    )
    CalculatorError.TankOverfilled -> stringResource(R.string.error_tank_full)
    is CalculatorError.TargetUnreachable -> stringResource(R.string.error_target_unreachable, target.toDisplayString())
    CalculatorError.MissingInput -> stringResource(R.string.error_missing_input)
}

private fun Double.toDisplayString(): String = NumberFormat.getNumberInstance().format(this)
