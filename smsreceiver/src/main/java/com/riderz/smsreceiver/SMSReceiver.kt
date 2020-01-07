package com.riderz.smsreceiver

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.Builder
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.tasks.Task

/**
 * mustContainWord : String : set word to should contain on message, (ignore Case = true)
 * otpLength: Int : set OTP length, default value = 6
 */
class SMSReceiver(
    private var activity: Activity,
    private var onSMSReceiverCallback: SMSBroadcastReceiver.OTPReceiveListener?,
    private val mustContainWord: String?,
    private val otpLength: Int? = null,
    private val msgAction: MessageAction? = null
) {
    private var apiClient: GoogleApiClient? = null

    companion object {
        var hashKey = ""
        var containWord: String = ""
        var length: Int = 6
        var messageAction: MessageAction? = null
    }

    init {
        loadApiClient()
    }

    private fun loadApiClient() {
        val appSignature =
            AppSignatureHelper(activity)
        hashKey = appSignature.getAppSignatures()?.get(0) ?: ""

        otpLength?.let {
            length = it
        }

        mustContainWord?.let {
            containWord = it
        }
        messageAction = msgAction

        apiClient = Builder(activity)
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
        val client = SmsRetriever.getClient(activity /* context */)
        val task: Task<*> = client.startSmsRetriever()
        // Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener { onSMSReceiverCallback?.onSMSReceiverStarted() }
        task.addOnFailureListener { e -> onSMSReceiverCallback?.onSMSReceiverFailed(e) }
    }
}

class MessageAction(
    val viewForSnackBar: View,
    val actionButtonText: String
)