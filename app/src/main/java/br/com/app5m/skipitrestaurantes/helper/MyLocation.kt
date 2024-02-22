package br.com.app5m.skipitrestaurantes.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import java.util.*

class MyLocation(private val context: Context) {
//
//    var timer1: Timer? = null
    var lm: LocationManager? = null
    var locationResult: LocationResult? = null
    var gpsEnabled = false
    var networkEnabled = false

    fun getLocation(result: LocationResult?): Boolean {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result
        if (lm == null) lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            networkEnabled = lm!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        //don't start listeners if no provider is enabled
        if (!gpsEnabled && !networkEnabled) return false
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (gpsEnabled) lm?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListenerGps
            )
            if (networkEnabled) lm?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                locationListenerNetwork
            )
        }
        GetLastLocation()
//        timer1 = Timer()
//        timer1!!.schedule(GetLastLocation(), 5000)
        return true
    }

    var locationListenerGps: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
//            timer1!!.cancel()
            locationResult?.getLocation(location)
            lm?.removeUpdates(this)
            lm?.removeUpdates(locationListenerNetwork)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }
    var locationListenerNetwork: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
//            timer1!!.cancel()
            locationResult?.getLocation(location)
            lm?.removeUpdates(this)
            lm?.removeUpdates(locationListenerGps)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    internal inner class GetLastLocation : TimerTask() {
        override fun run() {
            lm?.removeUpdates(locationListenerGps)
            lm?.removeUpdates(locationListenerNetwork)
            var netLoc: Location? = null
            var gpsLoc: Location? = null
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsEnabled) gpsLoc =
                    lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (networkEnabled) netLoc =
                    lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            //if there are both values use the latest one
            if (gpsLoc != null && netLoc != null) {
                if (gpsLoc.time > netLoc.time) locationResult?.getLocation(gpsLoc) else locationResult!!.getLocation(
                    netLoc
                )
                return
            }
            if (gpsLoc != null) {
                locationResult?.getLocation(gpsLoc)
                return
            }
            if (netLoc != null) {
                locationResult?.getLocation(netLoc)
                return
            }
            locationResult?.getLocation(null)
        }
    }

    abstract class LocationResult {
        abstract fun getLocation(location: Location?)
    }
}