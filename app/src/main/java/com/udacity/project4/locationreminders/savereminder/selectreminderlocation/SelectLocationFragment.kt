package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.BuildConfig
import kotlinx.android.synthetic.main.activity_reminder_description.*
import kotlinx.android.synthetic.main.fragment_select_location.*
import java.util.jar.Manifest


class SelectLocationFragment : BaseFragment() {

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var movecontroller: NavController
    private val REQUEST_LOCATION_PERMISSION = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

//        finish to add the map setup implementation
//         zoom to the user location after taking his permission
//         add style to the map
//         put a marker to location that the user selected


//         call this function after the user confirms on the selected location


        class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

            private lateinit var map: GoogleMap
            private lateinit var binding: FragmentSelectLocationBinding
            private val TAG = MapsActivity::class.java.simpleName
            private val REQUEST_LOCATION_PERMISSION = 1

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                val REQUEST_LOCATION_PERMISSION = 1
                binding = FragmentSelectLocationBinding.inflate(layoutInflater)
                setContentView(binding.root)

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)


                //        FINISH TO When the user confirms on the selected location,
                //         send back the selected location details to the view model
                binding.savelocation.setOnClickListener {onLocationSelected(map)  }
            }

            override fun onMapReady(googleMap: GoogleMap) {
                map = googleMap
                //These coordinates represent the latitude and longitude of the Googleplex.
                val latitude = 37.422160
                val longitude = -122.084270
                val zoomLevel = 15f

                val homeLatLng = LatLng(latitude, longitude)
                // add marker and cameria
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
                map.addMarker(MarkerOptions().position(homeLatLng))


                // FINISH TO ADD POI
                setPoiClick(map)
                enableMyLocation()




            }
            private fun onLocationSelected(map: GoogleMap) {
                //        FINISH TO When the user confirms on the selected location,
                //         send back the selected location details to the view model
                //         and navigate back to the previous fragment to save the reminder and add the geofence

                map.setOnMapLongClickListener { latLng ->
                    map.addMarker(MarkerOptions().position(latLng))
                    val latitude = _viewModel.latitude.value
                    val longitude = _viewModel.longitude.value
                    movecontroller.navigate(R.id.saveReminderFragment)
                }

            }


            override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.map_options, menu)
            }
// add setMapStyle(map) in onOptionsItemSelected of menu
            override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

                //finish to Change the map type based on the user's selection.


                R.id.normal_map -> {
                    map.mapType = GoogleMap.MAP_TYPE_NORMAL
                    true
                }
                R.id.hybrid_map -> {
                    map.mapType = GoogleMap.MAP_TYPE_HYBRID
                    true
                }
                R.id.satellite_map -> {
                    map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    true
                }
                R.id.terrain_map -> {
                    map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                    true
                }
                //Users should have the option to change map type from the toolbar items.

                R.id.special -> { setMapStyle(map) }
                else -> super.onOptionsItemSelected(item)
            }

            private fun setPoiClick(map: GoogleMap) {
                map.setOnPoiClickListener { poi ->
                    val poiMarker = map.addMarker(
                        MarkerOptions()
                            .position(poi.latLng)
                            .title(poi.name)
                    )
                    poiMarker.showInfoWindow()
                }
            }

            // finish to add style by map styling wizard
            private fun setMapStyle(map: GoogleMap) {
                try {
                    val success = map.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.map_style
                        )
                    )

                    if (!success) {
                        Log.e(ConstraintLayoutStates.TAG, "Style parsing failed.")
                    }
                } catch (e: Resources.NotFoundException) {
                    Log.e(ConstraintLayoutStates.TAG, "Can't find style. Error: ", e)
                }
            }
            private fun isPermissionGranted() : Boolean {
                return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
            }
            private fun enableMyLocation() {
                if (isPermissionGranted()) {
                    map.setMyLocationEnabled(true)
                }
                else {

                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION
                    )
                }
            }

            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<String>,
                grantResults: IntArray) {
                // Check if location permissions are granted and if so enable the
                // location data layer.
                if (requestCode == REQUEST_LOCATION_PERMISSION) {
                    if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        enableMyLocation()
                    }
                }
            }}





        return binding.root
}}





