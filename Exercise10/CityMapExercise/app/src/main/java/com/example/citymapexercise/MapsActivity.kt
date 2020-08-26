package com.example.citymapexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //val Kivesjärvi = LatLng(64.23.43N, 27.33.31E)
        val kivesjarvi = LatLng(64.23, 27.33)
        val peePeeIsland = LatLng(47.1131, -52.5019)
        val pooPooPoint = LatLng(47.3020, -122.0217)
        val tissiSaaret = LatLng(68.5543, 27.54)
        val pyllySaaret = LatLng(62.5641, 30.4434)

        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(kivesjarvi).title("Marker in Kivesjärvi"))
        mMap.addMarker(MarkerOptions().position(peePeeIsland).title("Marker in Pee Pee Island"))
        mMap.addMarker(MarkerOptions().position(pooPooPoint).title("Marker in Poo Poo Point"))
        mMap.addMarker(MarkerOptions().position(tissiSaaret).title("Marker in TissiSaaret"))
        mMap.addMarker(MarkerOptions().position(pyllySaaret).title("Marker in PyllySaaret"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kivesjarvi))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(peePeeIsland))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pooPooPoint))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tissiSaaret))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pyllySaaret))
    }
}
