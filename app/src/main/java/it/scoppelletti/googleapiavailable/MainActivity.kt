package it.scoppelletti.googleapiavailable

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.Task
import java.util.concurrent.CancellationException

const val TAG: String = "MyTest"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val status: Int
        val resolvable: Boolean
        val googleApi: GoogleApiAvailability

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        googleApi = GoogleApiAvailability.getInstance()

        // I would like not to use these APIs, I have inserted them only to log
        // the returned values
        status = googleApi.isGooglePlayServicesAvailable(this)
        resolvable = googleApi.isUserResolvableError(status)
        Log.d(TAG, """isGooglePlayServicesAvailable=$status
            |(resolvable=$resolvable)""".trimMargin().replace('\n', ' '))

        // I would like to use only this API
        googleApi.makeGooglePlayServicesAvailable(this)
            .addOnSuccessListener {
                Log.d(TAG, "Task succeded.")
            }
            .addOnCanceledListener {
                Log.d(TAG, "Task canceled.")
            }
            .addOnFailureListener { ex ->
                Log.e(TAG, "Task failed.", ex)
            }
            .addOnCompleteListener(::onGoogleApiAvailable)
    }

    private fun onGoogleApiAvailable(task: Task<Void>) {
        val ex: Throwable?

        if (task.isSuccessful) {
            Log.d(TAG, "Google Play services available.")
            return
        }

        ex = task.exception
        if (ex is CancellationException) {
            Log.w(TAG, "Activity has been completed before the task.")
            return
        }

        Log.e(TAG, "Google Play services not available.", ex)
        AlertDialog.Builder(this)
            .setTitle(android.R.string.dialog_alert_title)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Google Play services not available.")
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                if (!isFinishing) {
                    finish()
                }
            }
            .setCancelable(false)
            .show()
    }
}
