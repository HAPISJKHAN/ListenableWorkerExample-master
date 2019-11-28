package com.febaisi.listenableworkerexample.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.febaisi.listenableworkerexample.R
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat



class MyAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show()

//        val workManagerInstance = context?.let { WorkManager.getInstance(it) }
//        val locationListenableWorker = OneTimeWorkRequest.Builder(LocationListenableWorker::class.java).build()

//        workManagerInstance?.cancelAllWork()
//        workManagerInstance?.enqueueUniqueWork("LOCATION_WORKER", ExistingWorkPolicy.KEEP, locationListenableWorker)


//        val liveDataWorkInfo: LiveData<WorkInfo> = workManagerInstance?.getWorkInfoByIdLiveData(locationListenableWorker.id)!!
//        val workInfo = liveDataWorkInfo.value
//        if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
//                    val outputData = workInfo.outputData
//
//            val latitude = outputData.getString(LocationListenableWorker.LOCATION_LAT)
//            Log.i(LocationListenableWorker::class.toString(), "latitude $latitude")
//
//            val longitude = outputData.getString(
//                LocationListenableWorker.LOCATION_LONG)
//            Log.i(LocationListenableWorker::class.toString(), "longitude $longitude")
//
//            val time = convertTime(outputData.getLong(
//                LocationListenableWorker.LOCATION_TIME, 0))
//            Log.i(LocationListenableWorker::class.toString(), "time $time")
//
//
//                } else if (workInfo?.state == WorkInfo.State.FAILED) {
//
//            Log.i(LocationListenableWorker::class.toString(), "something_went_wrong ${context.getString(R.string.something_went_wrong)}")
//            Log.i(LocationListenableWorker::class.toString(), "check_logs ${context.getString(R.string.check_logs)}")
//        }

        ForegroundService.startService(context!!, "This is an test foreground service...")
    }

    private fun convertTime(longDate: Long) = SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Date(longDate))
}