package com.bb.builds.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildType
import com.bb.builds.service.AutomateBuildSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateScreenViewModel : ViewModel(), KoinComponent {
    private val automateBuildSystemService: AutomateBuildSystem by inject()

    private val _branches = MutableStateFlow<List<String>>(listOf())
    val branches = _branches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getBranches()
    }

    fun build(
        buildData: BuildData,
        buildType: BuildType,
    ) {
        viewModelScope.launch {
            when (buildType) {
                BuildType.MACOS -> {
                    automateBuildSystemService.buildMac(
                        buildData = buildData,
                    )
                }

                BuildType.ANDROID -> {
                    if (buildData.isBundleToBuild) {
                        automateBuildSystemService.buildBundleAndroid(
                            buildData = buildData,
                        )
                    } else {
                        automateBuildSystemService.buildAndroid(
                            buildData = buildData,
                        )
                    }
                }

                BuildType.IOS -> {
                    automateBuildSystemService.buildIOS(
                        buildData = buildData,
                    )
                }

                else -> {}
            }
        }
    }

    private fun getBranches() = viewModelScope.launch {
        _isLoading.update { true }
        _branches.value = automateBuildSystemService.getBranches()?.branches ?: emptyList()
        _isLoading.update { false }
    }

}
