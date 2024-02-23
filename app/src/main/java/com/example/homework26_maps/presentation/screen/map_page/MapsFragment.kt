package com.example.homework26_maps.presentation.screen.map_page

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.homework26_maps.R
import com.example.homework26_maps.databinding.FragmentMapsBinding
import com.example.homework26_maps.presentation.base_fragment.BaseFragment
import com.example.homework26_maps.presentation.event.MapsPageEvents
import com.example.homework26_maps.presentation.extensions.showSnackBar
import com.example.homework26_maps.presentation.screen.search_bottom_sheet.SearchBottomSheet
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate), OnMapReadyCallback, SearchBottomSheet.PlaceSearchListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationPermissionGranted = false  // this is flag for map to know about location permission granted
    private var country :String? = null
    private val viewModel: MapsPageViewModel by viewModels()

    companion object{
        private const val zoomNumber = 10f // smaller zoomNum, farther the camera
        private const val zoomNumberForConfigurationChange = 1f
    }

    private fun reMarkAllPlaces(){
        if(::mMap.isInitialized){
            viewModel.marksList.forEach{
                pinLocationAndZoomCameraOnMap(it, zoomNumberForConfigurationChange, getString(R.string.your_location))
                Log.d("tag123","got pinned again")
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        requestLocationPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all { it.value }
        if(granted)locationPermissionGranted = true
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    override fun bind() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun bindViewActionListeners() {
        bindClickListenerOnLocationBtn()
        bindClickListenerOnSearchBtn()
        bindClickListenerOnUnpinAllBtn()
    }

    private fun bindClickListenerOnUnpinAllBtn(){
        binding.apply {
            unPinAllBtn.setOnClickListener{
                country = null
                clearMapFormPins()
            }
        }
    }

    private fun clearMapFormPins(){
        if(::mMap.isInitialized){
            mMap.clear()
            viewModel.onEvent(MapsPageEvents.ClearAllMarks)
        }
    }

    private fun bindClickListenerOnLocationBtn(){
        binding.apply {
            locationBtn.setOnClickListener{
                getLastCachedLocation() // this will get the last cached device's location
            }
        }
    }

    private fun bindClickListenerOnSearchBtn(){
        binding.apply {
            searchBtn.setOnClickListener{
                val bottomSheet =  SearchBottomSheet(country?:"US").apply {
                    this.setListener(this@MapsFragment)
                }
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        reMarkAllPlaces()  // get from viewModel if there was anything
    }

    private fun getLastCachedLocation(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            binding.root.showSnackBar(getString(R.string.check_location_permissions))
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                setCountryByLocation(it)
                val lastLocation = LatLng(it.latitude, it.longitude)
                pinLocationAndZoomCameraOnMap(lastLocation, zoomNumber, getString(R.string.your_location)) // this will pin the location on map
                viewModel.onEvent(MapsPageEvents.AddMark(lastLocation))
            }
        }
    }

    private fun setCountryByLocation(location:Location){
        context?.let {cont->
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU){
                country = getCountryCodeFromLocationOld(cont,location)
            }else{
                //help
            }
        }
    }

    private fun pinLocationAndZoomCameraOnMap(location:LatLng, zoom:Float, label:String){
        mMap.addMarker(MarkerOptions().position(location).title(label))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }

    override fun onPlaceSelected(placeId: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                getLatLngFromPlaceId(placeId)
            }
        }
    }

    override fun onError(status: String) {
        if(status.isNotEmpty())binding.root.showSnackBar(status)
    }

    private fun getLatLngFromPlaceId(placeId:String){

        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        context?.let {
            val placesClient = Places.createClient(it)
            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                val latLng = place.latLng

                latLng?.let {latLngNonNull->
                    pinLocationAndZoomCameraOnMap(latLngNonNull, zoomNumber, getString(R.string.your_searched_location))
                    viewModel.onEvent(MapsPageEvents.AddMark(latLngNonNull))
                }

                Log.d("tag123", "Place found: ${place.name}, LatLng: $latLng")
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.d("tag123", "Place not found: ${exception.statusCode}")
                }
            }
        }
    }


    private fun getCountryCodeFromLocationOld(context: Context, location: Location): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?

        try {
            // Here, 1 represents the maximum number of results we want (the closest one).
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            Log.e("Geocoder", "Service Not Available", ioException)
            return null
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e("Geocoder", "Invalid Lat Long Used", illegalArgumentException)
            return null
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.isEmpty()) {
            Log.e("Geocoder", "No Address Found")
            return null
        }

        val address = addresses.first()
        // Return the country code; for example "US" or "FR"
        return address.countryCode
    }

}