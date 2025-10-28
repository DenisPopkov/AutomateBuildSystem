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
        isX86: Boolean,
    ) {
        viewModelScope.launch {
            when (buildType) {
                BuildType.MACOS -> {
                    automateBuildSystemService.buildMac(
                        buildData = buildData,
                        isX86 = isX86,
                    )
                }

                BuildType.ANDROID -> {
                    automateBuildSystemService.buildAndroid(
                        buildData = buildData,
                    )
                }

                BuildType.IOS -> {
                    automateBuildSystemService.buildIOS(
                        buildData = buildData,
                    )
                }

                BuildType.WINDOWS -> {
                    automateBuildSystemService.buildWin(
                        buildData = buildData,
                    )
                }
            }
        }
    }

    suspend fun rebuildDSPLibrary(
        branchName: String,
        isWindows: Boolean,
        isX86: Boolean,
        isUseDevAnalytics: Boolean,
    ) {
        automateBuildSystemService.rebuildDSPLibrary(
            BuildData(
                branchName = branchName,
                isUseDevAnalytics = isUseDevAnalytics,
            ),
            isWindows = isWindows,
            isX86 = isX86,
        )
    }

    suspend fun rebuildAndroidDSPLibrary(branchName: String, isUseDevAnalytics: Boolean) {
        automateBuildSystemService.rebuildAndroidDSPLibrary(
            BuildData(
                branchName = branchName,
                isUseDevAnalytics = isUseDevAnalytics,
            )
        )
    }

    suspend fun rebuildMacDSPBothArchitectures(branchName: String, isUseDevAnalytics: Boolean) {
        // Rebuild for x86
        rebuildDSPLibrary(
            branchName = branchName,
            isWindows = false,
            isX86 = true,
            isUseDevAnalytics = isUseDevAnalytics
        )
        // Rebuild for ARM (M1)
        rebuildDSPLibrary(
            branchName = branchName,
            isWindows = false,
            isX86 = false,
            isUseDevAnalytics = isUseDevAnalytics
        )
    }

    suspend fun rebuildJARLibrary(branchName: String) {
        automateBuildSystemService.rebuildJARLibrary(
            BuildData(
                branchName = branchName,
                isUseDevAnalytics = false,
            )
        )
    }

    private fun getBranches() = viewModelScope.launch {
        _isLoading.update { true }
        _branches.value = automateBuildSystemService.getBranches()?.branches ?: emptyList()
        _isLoading.update { false }
    }

}
