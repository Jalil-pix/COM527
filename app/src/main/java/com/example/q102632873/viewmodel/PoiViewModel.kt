package com.example.q102632873.viewmodel

import androidx.lifecycle.ViewModel
import com.example.q102632873.data.PointOfInterest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PoiViewModel : ViewModel() {

    private val _poiList = MutableStateFlow<List<PointOfInterest>>(emptyList())
    val poiList: StateFlow<List<PointOfInterest>> = _poiList

    private val _currentLat = MutableStateFlow(50.902614)
    val currentLat: StateFlow<Double> = _currentLat

    private val _currentLon = MutableStateFlow(-1.404464)
    val currentLon: StateFlow<Double> = _currentLon

    fun updateCurrentLocation(lat: Double, lon: Double) {
        _currentLat.value = lat
        _currentLon.value = lon
    }

    fun addPoi(
        name: String,
        type: String,
        description: String,
        code: String
    ): Boolean {
        if (name.isBlank() || type.isBlank() || description.isBlank()) {
            return false
        }

        if (!code.startsWith("0x4d4144")) {
            return false
        }

        val newPoi = PointOfInterest(
            id = _poiList.value.size + 1,
            name = name.trim(),
            type = type.trim(),
            description = description.trim(),
            lat = _currentLat.value,
            lon = _currentLon.value,
            code = code.trim()
        )

        _poiList.value = _poiList.value + newPoi
        return true
    }
}