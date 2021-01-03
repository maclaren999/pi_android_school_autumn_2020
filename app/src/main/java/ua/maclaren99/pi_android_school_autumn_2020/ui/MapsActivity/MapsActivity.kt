package ua.maclaren99.pi_android_school_autumn_2020.ui.MapsActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_maps.*
import ua.maclaren99.pi_android_school_autumn_2020.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        public const val KEY_LATITUDE = "latitude"
        public const val KEY_LONGITUDE = "longitude"
    }

    private val REQUEST_LOCATION_PERMISSION: Int = 2001
    val defaultZoom = 10F
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
//    private lateinit var currentLocation: LatLng
    private lateinit var currentLocation: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        initFunc()

    }

    private fun initFunc() {
        //SupportMapFragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initListeners() {
        flickr_search_button.setOnClickListener {
            startActivity(Intent(this, FlickrLocationActivity::class.java)
                .putExtra(KEY_LATITUDE, currentLocation.position.latitude)
                .putExtra(KEY_LONGITUDE, currentLocation.position.longitude)
            )
        }
        //Map click
        mMap.setOnMapClickListener {
            val marker = mMap.addMarker(MarkerOptions().position(it))
            currentLocation.remove()
            currentLocation = marker
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        mFusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                val marker = mMap.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(getString(R.string.your_location))
                )
                currentLocation = marker
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation.position, defaultZoom))
            }
        }
        initListeners()
    }


}