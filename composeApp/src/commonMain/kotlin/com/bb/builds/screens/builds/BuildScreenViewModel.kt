package com.bb.builds.screens.builds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bb.builds.domain.BuildId
import com.bb.builds.domain.BuildItem
import com.bb.builds.service.AutomateBuildSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BuildScreenViewModel : ViewModel(), KoinComponent {
    private val automateBuildSystemService: AutomateBuildSystem by inject()

    private val _builds = MutableStateFlow<List<BuildItem>>(listOf())
    val builds = _builds.asStateFlow()

    init {
        viewModelScope.launch {
            _builds.emit(getBuilds())
        }
    }

    fun send(buildId: BuildId) {
        viewModelScope.launch {
            automateBuildSystemService.sendBuild(buildId)
        }
    }

    private suspend fun getBuilds(): List<BuildItem> =
        automateBuildSystemService.getBuilds()

}
