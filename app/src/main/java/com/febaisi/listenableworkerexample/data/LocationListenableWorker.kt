package com.febaisi.listenableworkerexample.data

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.concurrent.futures.ResolvableFuture
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class LocationListenableWorker(appContext: Context, workerParams: WorkerParameters)
    : ListenableWorker(appContext, workerParams) {

    companion object { // Also available to JAVA classes
        @JvmStatic val LOCATION_LAT = "LOCATION_LAT"
        @JvmStatic val LOCATION_LONG = "LOCATION_LONG"
        @JvmStatic val LOCATION_TIME = "LOCATION_TIME"
    }

    private val future : ResolvableFuture<Result> = ResolvableFuture.create()

    override fun startWork(): ListenableFuture<Result> {

        CoroutineScope(Default).launch {
            //Getting out of MAIN Thread to set up location listener
            //This scope will be finished after setting up the listeners (Not wait through the final response)
            getLocation()
            Log.i(LocationListenableWorker::class.toString(), "Global score finished")
        }

        Log.i(LocationListenableWorker::class.toString(), "startWork returning future")
        return future
    }

    private fun getLocation() {
        LocationServices.getFusedLocationProviderClient(applicationContext)
            ?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                //MAIN THREAD - MINIMAL WORK
                //YOU MIGHT WANT TO PASS AN EXECUTOR TO THE ONSUCCESSLISTENER IF YOU HAVE A LOT OF WORK TO DO
                if (location != null) {
//                    future.set(Result.success(createOutputData(location)))
                    val latitude = location.latitude.toString()
                    Log.i(LocationListenableWorker::class.toString(), "latitude $latitude")

                    val longitude = location.longitude.toString()
                    Log.i(LocationListenableWorker::class.toString(), "longitude $longitude")

                    val time = location.time
                    Log.i(LocationListenableWorker::class.toString(), "time $time")
                } else {
                    Log.e(LocationListenableWorker::class.toString(), "Location null")
//                    future.set(Result.success(Data.EMPTY))
                }
            }
            ?.addOnFailureListener { exception ->
                Log.e(LocationListenableWorker::class.toString(), "Something went wrong getting the location -> $exception")
                future.set(Result.failure(Data.EMPTY))
            }
    }

    private fun createOutputData(location: Location): Data {

        val latitude = location.latitude.toString()
        Log.i(LocationListenableWorker::class.toString(), "latitude $latitude")

        val longitude = location.longitude.toString()
        Log.i(LocationListenableWorker::class.toString(), "longitude $longitude")

        val time = location.time
        Log.i(LocationListenableWorker::class.toString(), "time $time")

        return Data.Builder()
            .putString(LOCATION_LAT, latitude)
            .putString(LOCATION_LONG, longitude)
            .putLong(LOCATION_TIME, time)
            .build()
    }
}