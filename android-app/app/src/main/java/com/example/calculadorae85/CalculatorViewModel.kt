package com.example.calculadorae85

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CalculatorTab {
    SIMPLE, FILL, MONEY
}

sealed interface CalculatorError {
    data class TargetOutOfRange(
        val target: Double,
        val pctGasoline: Double,
        val pctAlcohol: Double
    ) : CalculatorError

    data object TankOverfilled : CalculatorError
    data class TargetUnreachable(val target: Double) : CalculatorError
    data object MissingInput : CalculatorError
}

data class CalculatorState(
    val activeTab: CalculatorTab = CalculatorTab.SIMPLE,
    val targetE85: String = "85",
    val pctGasoline: String = "27",
    val pctAlcohol: String = "100",
    
    // Aba: Simple
    val totalVolume: String = "50",
    
    // Aba: Fill
    val currentVolume: String = "10",
    val currentEthanolPct: String = "27",
    val tankCapacity: String = "50",
    
    // Aba: Money
    val totalMoney: String = "100",
    val priceGas: String = "5.50",
    val priceAlc: String = "3.50",
    
    // Resultados
    val resGasLiters: Double = 0.0,
    val resAlcLiters: Double = 0.0,
    val resGasMoney: Double = 0.0,
    val resAlcMoney: Double = 0.0,
    val error: CalculatorError? = null
)

class CalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onTabSelected(tab: CalculatorTab) {
        _state.update { it.copy(activeTab = tab) }
        calculate()
    }

    fun onTargetE85Changed(value: String) { _state.update { it.copy(targetE85 = value) }; calculate() }
    fun onPctGasolineChanged(value: String) { _state.update { it.copy(pctGasoline = value) }; calculate() }
    fun onPctAlcoholChanged(value: String) { _state.update { it.copy(pctAlcohol = value) }; calculate() }
    fun onTotalVolumeChanged(value: String) { _state.update { it.copy(totalVolume = value) }; calculate() }
    fun onCurrentVolumeChanged(value: String) { _state.update { it.copy(currentVolume = value) }; calculate() }
    fun onCurrentEthanolPctChanged(value: String) { _state.update { it.copy(currentEthanolPct = value) }; calculate() }
    fun onTankCapacityChanged(value: String) { _state.update { it.copy(tankCapacity = value) }; calculate() }
    fun onTotalMoneyChanged(value: String) { _state.update { it.copy(totalMoney = value) }; calculate() }
    fun onPriceGasChanged(value: String) { _state.update { it.copy(priceGas = value) }; calculate() }
    fun onPriceAlcChanged(value: String) { _state.update { it.copy(priceAlc = value) }; calculate() }

    // Accepts both "." and "," as decimal separator, regardless of device locale.
    private fun String.toDoubleLenient(): Double? = replace(',', '.').toDoubleOrNull()

    private fun calculate() {
        val s = _state.value
        val target = s.targetE85.toDoubleLenient() ?: 0.0
        val pGas = s.pctGasoline.toDoubleLenient() ?: 27.0
        val pAlc = s.pctAlcohol.toDoubleLenient() ?: 100.0

        if (target < pGas || target > pAlc) {
            _state.update { it.copy(error = CalculatorError.TargetOutOfRange(target, pGas, pAlc), resGasLiters = 0.0, resAlcLiters = 0.0) }
            return
        }

        val ratioGas = (pAlc - target) / (pAlc - pGas)
        val ratioAlc = (target - pGas) / (pAlc - pGas)

        var vGas = 0.0
        var vAlc = 0.0
        var mGas = 0.0
        var mAlc = 0.0
        var errorMsg: CalculatorError? = null

        when (s.activeTab) {
            CalculatorTab.SIMPLE -> {
                val total = s.totalVolume.toDoubleLenient() ?: 0.0
                vGas = total * ratioGas
                vAlc = total * ratioAlc
            }
            CalculatorTab.FILL -> {
                val currentVol = s.currentVolume.toDoubleLenient() ?: 0.0
                val currentPct = s.currentEthanolPct.toDoubleLenient() ?: 27.0
                val capacity = s.tankCapacity.toDoubleLenient() ?: 0.0
                val volumeToAdd = capacity - currentVol
                
                if (volumeToAdd <= 0) {
                    errorMsg = CalculatorError.TankOverfilled
                } else {
                    val currentEthanolVolume = currentVol * (currentPct / 100.0)
                    val targetEthanolVolume = capacity * (target / 100.0)
                    val ethanolNeeded = targetEthanolVolume - currentEthanolVolume
                    
                    vGas = (volumeToAdd * (pAlc / 100.0) - ethanolNeeded) / ((pAlc - pGas) / 100.0)
                    vAlc = volumeToAdd - vGas
                    
                    if (vGas < 0 || vAlc < 0) {
                        errorMsg = CalculatorError.TargetUnreachable(target)
                        vGas = 0.0
                        vAlc = 0.0
                    }
                }
            }
            CalculatorTab.MONEY -> {
                val money = s.totalMoney.toDoubleLenient() ?: 0.0
                val prGas = s.priceGas.toDoubleLenient() ?: 0.0
                val prAlc = s.priceAlc.toDoubleLenient() ?: 0.0
                
                if (prGas > 0 && prAlc > 0) {
                    val avgPrice = (ratioGas * prGas) + (ratioAlc * prAlc)
                    val totalLiters = money / avgPrice
                    vGas = totalLiters * ratioGas
                    vAlc = totalLiters * ratioAlc
                    mGas = vGas * prGas
                    mAlc = vAlc * prAlc
                }
            }
        }

        if (vGas == 0.0 && vAlc == 0.0 && errorMsg == null) {
            errorMsg = CalculatorError.MissingInput
        }

        _state.update { 
            it.copy(
                resGasLiters = vGas,
                resAlcLiters = vAlc,
                resGasMoney = mGas,
                resAlcMoney = mAlc,
                error = errorMsg
            )
        }
    }
}
