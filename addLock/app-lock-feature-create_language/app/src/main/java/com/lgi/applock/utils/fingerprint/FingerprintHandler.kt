package com.lgi.applock.utils.fingerprint

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

@RequiresApi(Build.VERSION_CODES.M)
class FingerprintHandler(private var context: Context) :
    FingerprintManager.AuthenticationCallback() {
    var cancellationSignal : CancellationSignal?=null

    // Constructor

    fun startAuth(
        manager: FingerprintManager,
        cryptoObject: FingerprintManager.CryptoObject?
    ) {
         cancellationSignal = CancellationSignal()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_FINGERPRINT
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)

    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {




    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {

    }

    override fun onAuthenticationFailed() {

    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        this.update()
    }

    private  fun update() {

            val intent = Intent("FINGER")

            context.sendBroadcast(intent)
        }
//        Toast.makeText(context,"ji",Toast.LENGTH_SHORT).show()


}
