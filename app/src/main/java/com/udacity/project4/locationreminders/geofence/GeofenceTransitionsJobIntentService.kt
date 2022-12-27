package com.udacity.project4.locationreminders.geofence

import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.core.app.JobIntentService
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.BuildConfig
import com.udacity.project4.R
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob
    private lateinit var binding: FragmentSelectLocationBinding
    companion object {
        private const val JOB_ID = 573

        //      finish to   call this to start the JobIntentService to handle the geofencing transition events


        class SimpleJobIntentService : JobIntentService() {

            //finish to handle the geofencing transition events and
            // send a notification to the user when he enters the geofence area
            // call @sendNotification
           // finish to get the request id of the current geofence at 22 -12-2022(9 pm)

            override fun onHandleWork(@NonNull intent: Intent) {
                // We have received work to do.  The system or framework is already
                // holding a wake lock for us at this point, so we can just go.
                Log.i("SimpleJobIntentService", "Executing work: $intent")
                var label = intent.getStringExtra("label")
                if (label == null) {
                    label = intent.toString()
                }
                toast("Executing: $label")
                for (i in 0..4) {
                    Log.i(
                        "SimpleJobIntentService", "Running service " + (i + 1)
                                + "/5 @ " + SystemClock.elapsedRealtime()
                    )
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                    }
                }
                Log.i(
                    "SimpleJobIntentService",
                    "Completed service @ " + SystemClock.elapsedRealtime()
                )
            }

            override fun onDestroy() {
                super.onDestroy()
                toast("All work complete")
            }

            val mHandler: Handler = Handler()

            // Helper for showing tests
            fun toast(text: CharSequence?) {
                mHandler.post(java.lang.Runnable {
                    Toast.makeText(
                        this@SimpleJobIntentService,
                        text,
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }



                /**
                 * Convenience method for enqueuing work in to this service.
                 */
                fun enqueueWork(context: Context?, work: Intent?) {
                    enqueueWork(
                        context,
                        SimpleJobIntentService::class.java, JOB_ID, work
                    )
                }
            }
        }




        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }



    private fun sendNotification(triggeringGeofences: List<Geofence>) {
        val requestId = ""

        //Get the local repository instance
        val remindersLocalRepository: ReminderDataSource by inject()
//        Interaction to the repository has to be through a coroutine scope
        CoroutineScope(coroutineContext).launch(SupervisorJob()) {
            //get the reminder with the request id
            val result = remindersLocalRepository.getReminder(requestId)
            if (result is Result.Success<ReminderDTO>) {
                val reminderDTO = result.data
                //send a notification to the user with the reminder details
                sendNotification(
                    this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                        reminderDTO.title,
                        reminderDTO.description,
                        reminderDTO.location,
                        reminderDTO.latitude,
                        reminderDTO.longitude,
                        reminderDTO.id
                    )
                )
            }
        }
    }
    @TargetApi(29 )
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(ConstraintLayoutStates.TAG, "onRequestPermissionResult")

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED))
        {
            Snackbar.make(
                binding.FragmentSelectLocationBinding,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            checkDeviceLocationSettingsAndStartGeofence()
        }
    }
    fun checkDeviceLocationSettingsAndStartGeofence(resolve:Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve){
                try {
                    exception.startResolutionForResult(this@SelectLocationFragment,
                        REQUEST_TURN_DEVICE_LOCATION_ON)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(ConstraintLayoutStates.TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    binding.FragmentSelectLocationBinding,
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeofence()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                addGeofenceForClue()
            }
        }}

/// add function description of an Intent and target action at 25/12/2022


    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


}











