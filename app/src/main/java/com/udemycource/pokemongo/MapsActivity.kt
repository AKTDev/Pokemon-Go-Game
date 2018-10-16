package com.udemycource.pokemongo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
        loadPokemon()
    }

    var AccessLocation = 123
    fun checkPermission()
    {
        if(Build.VERSION.SDK_INT >=23)
        {
            if(ActivityCompat
                            .checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),AccessLocation)
                return
            }
        }
            getUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation()
    {
        Toast.makeText(this,"User Location Access On",Toast.LENGTH_SHORT).show()

        var myLocation = myLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var thread = myThread()
        thread.start()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            AccessLocation ->  {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getUserLocation()
                }
                else
                {
                    Toast.makeText(this,"User Not Granted Permission",Toast.LENGTH_SHORT).show()

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }

    var location : Location? = null

    var playerPower = 0.0
    inner class myLocationListener : LocationListener
    {

        constructor()
        {
            location = Location("Start")
            location?.longitude = 0.0
            location?.longitude = 0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location = p0
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
             }

        override fun onProviderEnabled(provider: String?) {

             }

        override fun onProviderDisabled(provider: String?) {

             }

    }

    var oldLocation:Location? = null

    inner class myThread : Thread
    {
        constructor() : super()
        {
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.longitude = 0.0
        }

        @SuppressLint("MissingPermission")
        override fun run()
        {
            while (true)
            {
                try {
                    if(oldLocation!!.distanceTo(location) == 0f)
                    {
                        continue
                    }

                    oldLocation = location

                    runOnUiThread {

                        mMap.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("User")
                                .snippet("This is User's Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,22f))
                        mMap.uiSettings.setZoomControlsEnabled(true)
                        mMap.isMyLocationEnabled = true


                        for(i in 0..listPokemons.size-1) {
                            var newPokemon = listPokemons[i]
                            if (newPokemon.isCatched == false) {
                                val pokemonLoc = LatLng(newPokemon.location!!.latitude, newPokemon.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                        .position(pokemonLoc)
                                        .title(newPokemon.name)
                                        .snippet(newPokemon.des +" Power: " +newPokemon.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokemon.img!!)))


                                if(location!!.distanceTo(newPokemon.location) < 2)
                                {
                                    newPokemon.isCatched = true
                                    newPokemon = listPokemons[i]
                                    playerPower += newPokemon.power!!
                                    Toast.makeText(applicationContext,
                                            "Congo!! You Catched ${newPokemon.name}.Your Power Increased by ${newPokemon.power}",Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                    Thread.sleep(1000)
                }
                catch (ex : Exception)
                {

                }
            }

            super.run()

        }
    }

    var listPokemons = ArrayList<Pokemon>()
    fun loadPokemon()
    {
        listPokemons.add(Pokemon
        ("Bulbasaur","I am Bulbasur",R.drawable.bulbasaur,37.7789,-122.40190,55.9))
        listPokemons.add(Pokemon
        ("Charmander","I am Charmander",R.drawable.charmander,37.7785,-122.40183,65.9))
        listPokemons.add(Pokemon
        ("Squirtle","I am Squirtle",R.drawable.squirtle,37.7789,-122.40175,60.9))
    }

    }
