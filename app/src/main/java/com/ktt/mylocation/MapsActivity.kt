package com.ktt.mylocation

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ktt.mylocation.databinding.ActivityMapsBinding
import com.ktt.mylocation.user.MyDataBase
import com.ktt.mylocation.user.MyFactory
import com.ktt.mylocation.user.MyRepository
import com.ktt.mylocation.user.MyViewModel
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var myViewModel: MyViewModel
    private var lat = ""
    private var lan = ""
    private var id = ""
    private var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myRepository = MyRepository(MyDataBase.getDatabase(this))
        val factory = MyFactory(myRepository)
        myViewModel = ViewModelProvider(this, factory)[MyViewModel::class.java]
        lat = intent.getStringExtra("lat")!!
        lan = intent.getStringExtra("lan")!!
        id = intent.getStringExtra("id")!!

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        onLocationChanged()

        binding.history.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                getHistory(mMap)

            }
        }
    }

     fun getHistory(googleMap: GoogleMap) {

        lifecycleScope.launchWhenStarted {
            myViewModel.getUserLocation(id)!!.collect {

                for (i in it!!) {
                    mMap = googleMap
                    Log.i("TAG", "getLocaion: $it")

                    // Add a marker in Sydney and move the camera

                    val sydney = LatLng(i.lat.toDouble(), i.lan.toDouble())
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

                    val numMarkersInRainbow = 12
                    for (j in 0 until numMarkersInRainbow) {
                        mMap.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        i.lat.toDouble(),
                                        i.lan.toDouble()
                                    )
                                )

                                .title(address)
                                .icon(BitmapDescriptorFactory.defaultMarker((j * 360 / numMarkersInRainbow).toFloat()))
                        )
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(lat.toDouble(), lan.toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title(address))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val cameraupdate = CameraUpdateFactory.newLatLngZoom(sydney, 17f)
        googleMap.animateCamera(cameraupdate)
    }

    private fun onLocationChanged() {
        Toast.makeText(this, "$lat,$lan", Toast.LENGTH_SHORT).show()
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(lat.toDouble(), lan.toDouble(), 1) as List<Address>
            address = addresses[0].getAddressLine(0)
            Log.i("TAG", "onLocationChanged: $addresses")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}

