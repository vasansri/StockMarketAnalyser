package com.stockvision.core.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PremiumManager @Inject constructor() {
    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    fun setPremiumStatus(status: Boolean) {
        _isPremium.value = status
    }

    fun canUseFeature(feature: Feature): Boolean {
        return _isPremium.value || feature.isFree
    }

    enum class Feature(val isFree: Boolean) {
        BASIC_CHARTS(true),
        ADVANCED_INDICATORS(false),
        AI_PREDICTIONS(false),
        UNLIMITED_ALERTS(false)
    }
}
