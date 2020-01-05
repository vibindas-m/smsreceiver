# SMS Receiver with SMS Retriever API
This is simple library for receiving sms in android with new SMS Retriever API.

# Usage
### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```
### Step 2. Register receiver to your manifest file
```
<receiver android:name="com.riderz.smsreceiver.SMSBroadcastReceiver" android:exported="true">
	<intent-filter>
		<action android:name="com.google.android.gms.auth.api.phone.SMS_RETRIEVED"/>
	</intent-filter>
</receiver>
```
### Step 3. Instantiate SMSReceiver
```
SMSBroadcastReceiver.OTPReceiveListener smsReceiverCallback = new SMSBroadcastReceiver.OTPReceiveListener() {
	@Override
	public void onSMSReceiverStarted() {

	}

	@Override
	public void onSMSReceiverFailed(Exception exception) {

	}

	@Override
	public void onSMSReceived(String message) {

	}

	@Override
	public void onSMSReceiverTimeOut() {

	}
};

SMSReceiver smsReceiver = SMSReceiver(activity, smsReceiverCallback, mustContainWord, otpLength)
```
### Step 4. Call startSmsListener() method to start receiving
```
smsReceiver.startSmsListener() // It stops receiving after one message received
```
# Construct a verification message
The verification message that you will send to the user's device. This message must:

 - Be no longer than 140 bytes
 - Begin with the prefix <#>
 - Contain a one-time code that the client sends back to your server to complete the verification flow (see Generating a one-time code)
 - End with an 11-character hash string that identifies your app (see Computing your app's hash string)

Otherwise, the contents of the verification message can be whatever you choose. It is helpful to create a message from which you can easily extract the one-time code later on. For example, a valid verification message might look like the following:
```
Send SMS with the must contain word
<#> Your ExampleApp code is: 123ABC78
FA+9qCX9VSu
```
