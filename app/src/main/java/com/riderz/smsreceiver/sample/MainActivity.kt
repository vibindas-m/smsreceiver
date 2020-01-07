package com.riderz.smsreceiver.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pravinkumarputta.android.smsreceiverdemo.R
import com.riderz.smsreceiver.MessageAction
import com.riderz.smsreceiver.SMSBroadcastReceiver
import com.riderz.smsreceiver.SMSReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SMSBroadcastReceiver.OTPReceiveListener {
    private var smsReceiver: SMSReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btWithAction.setOnClickListener {
            val messageAction = MessageAction(layoutParent, "SET")
            smsReceiver =
                SMSReceiver(this, this, etMustContainWord.text.toString(), 6, messageAction)
            smsReceiver?.startSmsListener()
        }

        btWithOutAction.setOnClickListener {
            smsReceiver =
                SMSReceiver(this, this, etMustContainWord.text.toString(), 6)
            smsReceiver?.startSmsListener()
        }
    }

    override fun onSMSReceiverStarted() {
        btWithAction.isEnabled = false
        btWithOutAction.isEnabled = false
        tvOtpResponse.text = "Waiting for the OTP"
    }

    override fun onSMSReceiverFailed(exception: Exception?) {
        btWithAction.isEnabled = true
        btWithOutAction.isEnabled = true
        tvOtpResponse.text = "Failed to Start SMS Retriever"
    }

    override fun onSMSReceived(message: String?) {
        btWithAction.isEnabled = true
        btWithOutAction.isEnabled = true
        tvOtpResponse.text = message
    }

    override fun onSMSButtonAction(message: String?) {
        btWithAction.isEnabled = true
        btWithOutAction.isEnabled = true
        tvOtpResponse.text = message
    }

    override fun onSMSReceiverTimeOut() {
        btWithAction.isEnabled = true
        btWithOutAction.isEnabled = true
        tvOtpResponse.text = "Otp receiver is expired"
    }
}
