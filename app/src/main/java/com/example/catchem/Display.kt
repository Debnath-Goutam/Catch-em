package com.example.catchem

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.catchem.databinding.ActivityDisplayBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.lang.Exception

class Display : AppCompatActivity(), OnMapReadyCallback {

    var usr_name=""
    var myPower=0.0
    var c=0;
    var current_lat:Double=0.0
    var current_long: Double=0.0
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        usr_name=intent.getStringExtra(Data.usr_name).toString()
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
    }

    var accessLocation=123

    fun checkPermission() {
        if(Build.VERSION.SDK_INT>=23) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),accessLocation)
                return
            }
        }

        getLocation()
    }

    fun getLocation() {
        var myLocation=MyLocation()
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3, 3f, myLocation)
        var myThread=updateThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            accessLocation ->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    getLocation()
                else
                    Toast.makeText(this, "Cannot access your location", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }

    var currentLocation: Location?=null

    inner class MyLocation: LocationListener {
        constructor() {
            //Toast.makeText(applicationContext, "ok", Toast.LENGTH_SHORT).show()
            currentLocation= Location("start")
            currentLocation!!.latitude=0.0
            currentLocation!!.longitude=0.0
        }
        override fun onLocationChanged(location: Location) {
            currentLocation=location
            current_lat=currentLocation!!.latitude
            current_long=currentLocation!!.longitude
            c++;
            if(c==1)
                loadPokemons()
        }

    }

    var old_Locattion: Location?=null

    inner class updateThread: Thread {
        constructor(): super() {
            old_Locattion= Location("start")
            old_Locattion!!.longitude=0.0
            old_Locattion!!.latitude=0.0
        }
        override fun run() {
            while (true) {
                try {
                    if(old_Locattion!!.distanceTo(currentLocation)==0f)
                        continue
                    old_Locattion=currentLocation
                    runOnUiThread() {
                        //my location
                        mMap!!.clear()

                        val sydney = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                        Log.i("me", "lat: ${currentLocation!!.latitude}    long: ${currentLocation!!.longitude}")
                        mMap.addMarker(MarkerOptions().position(sydney).title(usr_name).snippet("Current power is: $myPower").icon(BitmapDescriptorFactory.fromResource(R.drawable.ash1)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16f))

                        //pokemon location
                        for(i in 0..poke_list.size-1) {
                            var new_poke = poke_list[i]
                            val sydney = LatLng(new_poke.loc_poke!!.latitude, new_poke.loc_poke!!.longitude)
                            mMap.addMarker(MarkerOptions().position(sydney).
                            title(new_poke.name!!).
                            snippet("Type: ${new_poke.type!!}    Power: ${new_poke.power}").
                            icon(BitmapDescriptorFactory.fromResource(new_poke.img!!)))
                            Log.i("${new_poke.name}", "lat: ${new_poke.loc_poke!!.latitude} long: ${new_poke.loc_poke!!.longitude}")

                            if (currentLocation!!.distanceTo(new_poke.loc_poke!!)<50) {
                                myPower+=new_poke.power!!
                                new_poke.loc_poke!!.latitude=rand_lat()
                                new_poke.loc_poke!!.longitude=rand_long()
                                poke_list[i]=new_poke

                                Toast.makeText(applicationContext, "Your caught a ${new_poke.name} Your current power is $myPower", Toast.LENGTH_LONG).show()
                            }

                        }
                    }
                    Thread.sleep(2000)
                }catch (e:Exception) {Log.i("Exception: ", "$e")}
            }
        }
    }

    var poke_list=ArrayList<Pokemons>()

    fun loadPokemons() {
        poke_list.add(Pokemons("Bulbasaur", "Grass", 20.0, rand_lat(), rand_long(),R.drawable.bulbasaur))
        poke_list.add(Pokemons("Charmander", "Fire", 21.0, rand_lat(), rand_long(),R.drawable.charmander))
        poke_list.add(Pokemons("Darkrai", "Dark", 37.0, rand_lat(), rand_long(),R.drawable.darkrai))
        poke_list.add(Pokemons("Eevee", "Normal", 22.0, rand_lat(), rand_long(),R.drawable.eevee))
        poke_list.add(Pokemons("Pikachu", "Electric", 22.0, rand_lat(), rand_long(),R.drawable.pikachu))
        poke_list.add(Pokemons("Raichu", "Electric", 32.0, rand_lat(), rand_long(),R.drawable.raichu))
        poke_list.add(Pokemons("Squirtle", "Water", 20.0, rand_lat(), rand_long(),R.drawable.squirtle))
        poke_list.add(Pokemons("Umbreon", "Dark", 33.0, rand_lat(), rand_long(),R.drawable.umbreon))
    }

    fun rand_lat(): Double {
        var x:Int=(Math.random()*10).toInt()
        var y=(Math.random()/100)
        if(x%2==0)
            return (current_lat+y)
        else
            return (current_lat-y)
    }

    fun rand_long(): Double {
        var x:Int=(Math.random()*10).toInt()
        var y=(Math.random()/100)
        if(x%2==0)
            return (current_long+y)
        else
            return (current_long-y)
    }
}