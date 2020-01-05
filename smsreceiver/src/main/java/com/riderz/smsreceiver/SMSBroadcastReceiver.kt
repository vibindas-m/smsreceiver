package com.riderz.smsreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private var otpReceiver: OTPReceiveListener? = null
        fun initOTPListener(receiver: OTPReceiveListener?) {
            otpReceiver = receiver
        }
    }

    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras ?: return
            val status = extras[SmsRetriever.EXTRA_STATUS] as Status? ?: return
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    Log.d("OTP_Message", message)
                    // Extract one-time code from the message and complete verification
// by sending the code back to your server for SMS authenticity.
// But here we are just passing it to MainActivity
                    if (otpReceiver != null && SMSUtils.checkMustContainWord(message, SMSReceiver.containWord)) {
                        val otp = SMSUtils.parseOTP(message, SMSReceiver.length)
                        if(otp.isNotEmpty()) {
                            otpReceiver?.onSMSReceived(
                                otp
                            )
                        }
                    }
                }
                CommonStatusCodes.TIMEOUT ->  // Waiting for SMS timed out (5 minutes)
// Handle the error ...
                    otpReceiver?.onSMSReceiverTimeOut()
            }
        }
    }

    interface OTPReceiveListener {
        fun onSMSReceiverStarted()
        fun onSMSReceiverFailed(exception: Exception?)
        fun onSMSReceived(message: String?)
        fun onSMSReceiverTimeOut()
    }
}