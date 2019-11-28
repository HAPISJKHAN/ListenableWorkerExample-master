package com.febaisi.listenableworkerexample.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.febaisi.listenableworkerexample.R
import com.febaisi.listenableworkerexample.ui.MainActivity
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForegroundService: Service() {

    private val CHANNEL_ID = "ForegroundService Kotlin"

    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Kotlin Example")
            .setContentText(input)
            .setSmallIcon(R.mipmap.ic_launcher_round)
//            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        CoroutineScope(Dispatchers.Default).launch {
            //Getting out of MAIN Thread to set up location listener
            //This scope will be finished after setting up the listeners (Not wait through the final response)
            getLocation()
            Log.i(ForegroundService::class.toString(), "Global score finished")
        }

        stopService(context = this);
        return START_NOT_STICKY
    }

    private fun getLocation() {
        LocationServices.getFusedLocationProviderClient(applicationContext)
            ?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                //MAIN THREAD - MINIMAL WORK
                //YOU MIGHT WANT TO PASS AN EXECUTOR TO THE ONSUCCESSLISTENER IF YOU HAVE A LOT OF WORK TO DO
                if (location != null) {
                    val latitude = location.latitude.toString()
                    Log.i(ForegroundService::class.toString(), "latitude $latitude")

                    val longitude = location.longitude.toString()
                    Log.i(ForegroundService::class.toString(), "longitude $longitude")

                    val time = location.time
                    Log.i(ForegroundService::class.toString(), "time $time")
                } else {
                    Log.e(ForegroundService::class.toString(), "Location null")
                }
            }
            ?.addOnFailureListener { exception ->
                Log.e(ForegroundService::class.toString(), "Something went wrong getting the location -> $exception")
            }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}