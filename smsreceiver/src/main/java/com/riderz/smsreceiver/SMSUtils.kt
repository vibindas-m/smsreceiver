package com.riderz.smsreceiver

import java.util.regex.Pattern

object SMSUtils {
    fun parseOTP(message: String?, length: Int): String {
        if (message.isNullOrEmpty()) {
            return ""
        }
        val lengthStr = length.toString()
        val pattern: Pattern = Pattern.compile("(\\d{$lengthStr})")
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(0)
        } else ""
    }

    fun checkMustContainWord(message: String?, containWord: String?): Boolean {
        return if (containWord.isNullOrEmpty()) {
            true
        } else {
            message?.contains(
                containWord, true
            ) == true
        }
    }
}