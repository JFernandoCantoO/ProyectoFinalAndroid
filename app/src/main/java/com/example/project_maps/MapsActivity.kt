package com.example.project_maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.project_maps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var latval: String = ""
    var longval: String = ""
    private var count: Int = 1

    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val referenciaDB : DatabaseReference = database.getReference("Coordenadas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.saveBttn.setOnClickListener{
            referenciaDB.child("coordenadas").child("Marcador $count").setValue("Latitud: {$latval}, Longitud:{$longval}")
            count++
        }

        binding.delBttn.setOnClickListener{
            referenciaDB.child("coordenadas").removeValue()
            count = 1
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val marca = LatLng(39.9890 , 116.3207)
        mMap.addMarker(
            MarkerOptions().position(marca)
                .title("Latitud: ${marca.latitude} y Longitud: ${marca.longitude}")
                .draggable(true)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marca))
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marcadorNuevo: Marker) {

            }

            override fun onMarkerDragEnd(marcadorNuevo: Marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marcadorNuevo.position, 1.0f))
                val msg = "Latitud: ${marcadorNuevo.position.latitude.toString().dropLast(2)}, Longitud: ${marcadorNuevo.position.longitude.toString().dropLast(2)}°"
                latval = marcadorNuevo.position.latitude.toString()
                longval = marcadorNuevo.position.longitude.toString()
                marcadorNuevo.title = msg
            }

            override fun onMarkerDrag(marcadorNuevo: Marker) {

            }
        })
    }
}