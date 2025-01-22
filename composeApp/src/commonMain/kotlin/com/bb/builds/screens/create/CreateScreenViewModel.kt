package com.bb.builds.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildType
import com.bb.builds.service.AutomateBuildSystem
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateScreenViewModel : ViewModel(), KoinComponent {
    private val automateBuildSystemService: AutomateBuildSystem by inject()

    fun buildMac(
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

                else -> {}
            }
        }
    }

}
