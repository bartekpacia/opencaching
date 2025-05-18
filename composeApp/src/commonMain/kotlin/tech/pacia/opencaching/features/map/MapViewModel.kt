package tech.pacia.opencaching.features.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import tech.pacia.okapi.client.models.BoundingBox
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.opencaching.data.CachesRepository
import tech.pacia.opencaching.debugLog
import kotlin.time.Duration.Companion.seconds


class MapViewModel : ViewModel {
    private val debounceDuration = 3.seconds

    private val cachesRepository: CachesRepository

    private val _geocaches = MutableStateFlow(mapOf<String, Geocache>())
    val geocaches: StateFlow<Map<String, Geocache>> = _geocaches

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    var lastInstant: Instant? = null

    constructor(cachesRepository: CachesRepository) {
        this.cachesRepository = cachesRepository
        debugLog("MapViewModel", "created!")
    }

    fun fetch(boundingBox: BoundingBox) {
        viewModelScope.launch {
            val currentInstant = Clock.System.now()
            val lastInstant = this@MapViewModel.lastInstant
            if (lastInstant != null) {
                val duration = currentInstant - lastInstant
                if (duration < debounceDuration) {
                    debugLog("MapViewModel", "map bounds changed, but less than $debounceDuration passed")
                    return@launch
                }
            }
            this@MapViewModel.lastInstant = currentInstant
            debugLog("MapViewModel", "map bounds changed, updating...")

            _isUpdating.value = true
            _geocaches.value = cachesRepository.searchAndRetrieve(boundingBox)
            _isUpdating.value = false
        }
    }
}