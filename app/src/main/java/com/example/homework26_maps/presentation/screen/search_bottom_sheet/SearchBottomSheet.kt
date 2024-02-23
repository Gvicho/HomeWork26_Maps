package com.example.homework26_maps.presentation.screen.search_bottom_sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homework26_maps.R
import com.example.homework26_maps.databinding.BottomSheetPlaceSearchBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchBottomSheet(
    private val country:String,

): BottomSheetDialogFragment() {

    private var _binding: BottomSheetPlaceSearchBinding? = null
    private val binding get() = _binding!!

    interface PlaceSearchListener {
        fun onPlaceSelected(placeId: String)
        fun onError(status: String)
    }

    private var listener: PlaceSearchListener? = null

    fun setListener(listener: PlaceSearchListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetPlaceSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.placesApiContainer) as AutocompleteSupportFragment

        autocompleteFragment.setTypesFilter(listOf(getString(R.string.address)))
        autocompleteFragment.setCountries(country)
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Handle the selected place
                place.id?.let {
                    listener?.onPlaceSelected(it) // give it to the Maps Fragment
                }

                dismiss()
            }

            override fun onError(status: Status) {
                listener?.onError(status.statusMessage?:"")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}