package com.example.golfcoursesinamap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.info_window.view.*
import org.json.JSONArray

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val TAG = "Ebin error"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        private val contents: View = layoutInflater.inflate(R.layout.info_window, null)

        override fun getInfoWindow(marker: Marker?): View? {
            return null
        }

        override fun getInfoContents(marker: Marker): View {
            // UI elements
            val titleTextView = contents.titleTextView
            val addressTextView = contents.addressTextView
            val phoneTextView = contents.phoneTextView
            val emailTextView = contents.emailTextView
            val webTextView = contents.webTextView
            // title
            titleTextView.text = marker.title.toString()
            // get data from Tag list
            if (marker.tag is List<*>){
                val list: List<String> = marker.tag as List<String>
                addressTextView.text = list[0]
                phoneTextView.text = list[1]
                emailTextView.text = list[2]
                webTextView.text = list[3]
            }
            return contents
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        loadData()
    }

    private fun loadData() {
        val queue = Volley.newRequestQueue(this)
        // 1. code here
        val url = "https://ptm.fi/materials/golfcourses/golf_courses.json"
        var golf_courses: JSONArray
        var course_types: Map<String, Float> = mapOf(
                "?" to BitmapDescriptorFactory.HUE_VIOLET,
                "Etu" to BitmapDescriptorFactory.HUE_BLUE,
                "Kulta" to BitmapDescriptorFactory.HUE_GREEN,
                "Kulta/Etu" to BitmapDescriptorFactory.HUE_YELLOW
        )

        // create JSON request object
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    // JSON loaded successfully
                    // 2. code here
                    golf_courses = response.getJSONArray("courses")
// loop through all objects
                    for (i in 0 until golf_courses.length()){
                        // get course data
                        val course = golf_courses.getJSONObject(i)
                        val lat = course["lat"].toString().toDouble()
                        val lng = course["lng"].toString().toDouble()
                        val coord = LatLng(lat, lng)
                        val type = course["type"].toString()
                        val title = course["course"].toString()
                        val address = course["address"].toString()
                        val phone = course["phone"].toString()
                        val email = course["email"].toString()
                        val web_url = course["web"].toString()

                        if (course_types.containsKey(type)){
                            var m = mMap.addMarker(
                                    MarkerOptions()
                                            .position(coord)
                                            .title(title)
                                            .icon(BitmapDescriptorFactory
                                                    .defaultMarker(course_types.getOrDefault(type, BitmapDescriptorFactory.HUE_RED)
                                                    )
                                            )
                            )
                            // pass data to marker via Tag
                            val list = listOf(address, phone, email, web_url)
                            m.tag = list
                        } else {
                            Log.d(TAG, "This course type does not exist in evaluation ${type}")
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(65.5, 26.0),5.0F))
                },
                Response.ErrorListener { error ->
                    // Error loading JSON
                    Log.d(TAG, "Error loading JSON: ${error}")
                }
        )
        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest)
        // Add custom info window adapter
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
    }
}
