package com.bb.builds.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildItem
import com.bb.builds.domain.BuildType
import com.bb.builds.domain.getBuildScript
import com.bb.builds.service.AutomateBuildSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateScreenViewModel : ViewModel(), KoinComponent {
    private val automateBuildSystemService: AutomateBuildSystem by inject()

    private val _builds = MutableStateFlow<List<BuildItem>>(listOf())
    val builds = _builds.asStateFlow()

    private val _branches = MutableStateFlow<List<String>>(listOf())
    val branches = _branches.asStateFlow()

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
                    automateBuildSystemService.buildAndroid(
                        buildData = buildData,
                    )
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

    fun stopBuild(
        buildType: BuildType,
    ) {
        viewModelScope.launch {
            if (buildType == BuildType.WINDOWS || buildType == BuildType.MACOS) {
                automateBuildSystemService.stopProcess(getBuildScript(buildType, false))
                automateBuildSystemService.stopProcess(getBuildScript(buildType, true))
            } else {
                automateBuildSystemService.stopProcess(getBuildScript(buildType, true))
            }
        }
    }

    private fun getBranches() = viewModelScope.launch {
        _branches.value = automateBuildSystemService.getBranches().branches
    }

}
