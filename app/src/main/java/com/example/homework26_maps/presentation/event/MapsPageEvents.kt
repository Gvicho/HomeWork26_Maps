package com.example.homework26_maps.presentation.event

import com.google.android.gms.maps.model.LatLng

sealed class MapsPageEvents {
    data class AddMark(val latLng:LatLng):MapsPageEvents()
    data object ClearAllMarks:MapsPageEvents()
}