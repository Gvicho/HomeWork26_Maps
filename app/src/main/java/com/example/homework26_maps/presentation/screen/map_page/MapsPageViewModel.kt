package com.example.homework26_maps.presentation.screen.map_page

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.homework26_maps.presentation.event.MapsPageEvents
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapsPageViewModel @Inject constructor():ViewModel() {

    private var _marksList : MutableList<LatLng> = mutableListOf()
    val marksList get() =  _marksList



    fun onEvent(event: MapsPageEvents){
        when(event){
            is MapsPageEvents.AddMark -> addMark(event.latLng)
            MapsPageEvents.ClearAllMarks -> clearMarks()
        }
    }

    private fun addMark(latLng: LatLng){
        _marksList.add(latLng)
        Log.d("tag123","added size : ${_marksList.size}")
    }

    private fun clearMarks(){
        _marksList.clear()
    }

}