package com.pravinkumarputta.android.smsreceiver

import com.riderz.smsreceiver.SMSUtils
import org.junit.Assert
import org.junit.Test

class SMSUtilsTest {
    @Test
    fun parseOTPTest() {
        Assert.assertEquals("", SMSUtils.parseOTP("The SMS-OTP 7654 is", 6))
        Assert.assertEquals("", SMSUtils.parseOTP("The SMS-OTP 76547 6 is", 6))
        Assert.assertEquals("", SMSUtils.parseOTP(null, 6))
        Assert.assertEquals("402814", SMSUtils.parseOTP("The SMS-OTP is 402814", 6))
    }

    @Test
    fun checkMustContainWordTest() {
        Assert.assertEquals(true, SMSUtils.checkMustContainWord("", ""))
        Assert.assertEquals(true, SMSUtils.checkMustContainWord("OTP", "OTP"))
        Assert.assertEquals(true, SMSUtils.checkMustContainWord("OTP", ""))
        Assert.assertEquals(true, SMSUtils.checkMustContainWord("message contain otp", "OTP"))
        Assert.assertEquals(false, SMSUtils.checkMustContainWord("message contain", "OTP"))
        Assert.assertEquals(false, SMSUtils.checkMustContainWord(null, "OTP"))
        Assert.assertEquals(true, SMSUtils.checkMustContainWord(null, null))
        Assert.assertEquals(true, SMSUtils.checkMustContainWord("message", null))
    }
}