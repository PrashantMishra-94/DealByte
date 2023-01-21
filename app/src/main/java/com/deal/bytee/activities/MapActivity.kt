package com.deal.bytee.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.deal.bytee.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        latitude = intent.getDoubleExtra(LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(LONGITUDE, 0.0)
        title = intent.getStringExtra(TITLE) ?: ""
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Enable the zoom controls for the map
        mMap!!.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap!!.isMyLocationEnabled = true
        }
        mMap!!.uiSettings.isZoomGesturesEnabled = true
        mMap!!.setOnMapLoadedCallback {
            setMarker()
        }
    }

    private fun setMarker() {
        mMap!!.addMarker(
            MarkerOptions().title(title).position(
                LatLng(latitude, longitude)
            ).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
        )?.let { animateCamera(it) }
    }

    private fun animateCamera(marker: Marker) {
        val builder = LatLngBounds.Builder()
        builder.include(marker.position)
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
    }

    companion object {
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
        const val TITLE = "TITLE"
    }
}