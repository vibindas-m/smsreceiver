package com.riderz.smsreceiver

import android.app.Activity
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.Builder
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.tasks.Task

class SMSReceiver(
    private var activity: Activity?,
    private var onSMSReceiverCallback: SMSBroadcastReceiver.OTPReceiveListener?
) {
    private var apiClient: GoogleApiClient? = null

    companion object {
        var hashKey = ""
    }
    init {
        loadApiClient()
    }

    private fun loadApiClient() {
        val appSignature =
            AppSignatureHelper(activity)
        hashKey = appSignature.getAppSignatures()?.get(0) ?: ""
        apiClient = Builder(activity!!)
            .addConnectionCallbacks(object : ConnectionCallbacks {
                override fun onConnected(@Nullable bundle: Bundle?) {}
                override fun onConnectionSuspended(i: Int) {}
            })
            .enableAutoManage(
                activity as FragmentActivity
            ) { connectionResult ->
                onSMSReceiverCallback?.onSMSReceiverFailed(
                    Exception(
                        connectionResult.errorMessage
                    )
                )
            }
            .addApi(Auth.CREDENTIALS_API)
            .build()
        //        startSmsListener();
        SMSBroadcastReceiver.initOTPListener(
            onSMSReceiverCallback
        )
    }

    fun startSmsListener() {
        val client = SmsRetriever.getClient(activity!! /* context */)
        val task: Task<*> = client.startSmsRetriever()
        // Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener { onSMSReceiverCallback?.onSMSReceiverStarted() }
        task.addOnFailureListener { e -> onSMSReceiverCallback?.onSMSReceiverFailed(e) }
    }
}