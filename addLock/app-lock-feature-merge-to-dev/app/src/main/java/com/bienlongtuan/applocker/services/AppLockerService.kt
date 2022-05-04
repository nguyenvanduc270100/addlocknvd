package com.bienlongtuan.applocker.services

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.*
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.data.db.DbApp
import com.bienlongtuan.applocker.data.model.DBAppLock
import com.bumptech.glide.Glide
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.bienlongtuan.applocker.databinding.ActivityScreenLockBinding
import com.bienlongtuan.applocker.utils.preferences.LockPreferences
import javax.inject.Inject

class AppLockerService : Service() {
    var keyStore: KeyStore? = null
    @Inject
    lateinit var sharedPreferences: LockPreferences

    // Variable used for storing the key in the Android Keystore container
    val KEY_NAME = "androidHive"
    var cipher: Cipher? = null
    var params: WindowManager.LayoutParams? = null
    var popupView: ActivityScreenLockBinding? = null
    val CHANNEL_ID = "com.bienlongtuan.applocker"
    var manager: NotificationManager? = null
    var notification: Notification? = null
    var windowManager: WindowManager? = null
    var mListAppLock = ArrayList<DBAppLock>()
    var dbApp: DbApp? = null

    var timer: Timer? = null

    companion object {
        var mCurrentApp = ""
        var mPackageName = ""
        var i = 0
        var k = 0

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    override fun onCreate() {
        super.onCreate()
        dbApp = DbApp(applicationContext, null)
        sharedPreferences = LockPreferences()
        mListAppLock = dbApp!!.getLocked()
        if (mListAppLock.size == 0) {
            mListAppLock.add(DBAppLock("App Lock", "com.bienlongtuan.applocker", "", 1, 0))
        }
        val intent = IntentFilter()
        intent.addAction("LOCKED")
        intent.addAction("LOCKEDAPP")
        intent.addAction("PAKAGENAME")
        intent.addAction("CHANGE")
        intent.addAction("FINGER")
        intent.addAction("FINGER_ERROR")
        applicationContext.registerReceiver(broadcastReceiver, intent)
        setUp()
        createNotificationChannel()


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer!!.cancel()
        timer = null
        if (popupView != null) {
            windowManager!!.removeViewImmediate(popupView?.root)
        }
        windowManager = null
    }

    private var broadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1?.action

//
            if (action!!.equals("PAKAGENAME", ignoreCase = true)) {

                val lock = p1.extras?.getBoolean("lock")
                if (lock!!) {

                    if (mCurrentApp != "app") {
                        setUpFinger()
                        if (i == 0) {
                            if (sharedPreferences.pin == true) {
                                popupView!!.tvPassPin.text =
                                    resources.getString(R.string.enter_pin)

                                Glide
                                    .with(applicationContext)
                                    .load(
                                        applicationContext.packageManager.getApplicationIcon(
                                            mPackageName
                                        )
                                    )
                                    .thumbnail(0.5f)
                                    .into(popupView!!.ivPin)


                            } else {
                                popupView!!.tvPass.text =
                                    resources.getString(R.string.draw_pattern)
                                Glide
                                    .with(applicationContext)
                                    .load(
                                        applicationContext.packageManager.getApplicationIcon(
                                            mPackageName
                                        )
                                    )
                                    .thumbnail(0.5f)
                                    .into(popupView!!.ivPattern)
                            }
                            i = 1
                        }
                        if (k > 0) {

                            popupView!!.container.visibility = View.VISIBLE


                            windowManager!!.updateViewLayout(popupView?.root, params)
                        }
//
                    }


                } else {
                    i = 0


                    mCurrentApp = ""
                    popupView!!.container.visibility = View.GONE
                    windowManager!!.updateViewLayout(popupView!!.root, params)

                }
//                Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show()
            } else if (action.equals("LOCKED", ignoreCase = true) || action.equals(
                    "LOCKEDAPP",
                    ignoreCase = true
                )
            ) {

                mListAppLock = dbApp!!.getLocked()


            } else if (action.equals("CHANGE", ignoreCase = true)) {
                val pattern = p1.extras!!.getBoolean("pattern")
                if (!pattern) {
                    popupView!!.llPattern.visibility = View.GONE
                    popupView!!.rlPin.visibility = View.VISIBLE
                    setUpPin()
                } else {
                    popupView!!.llPattern.visibility = View.VISIBLE
                    popupView!!.rlPin.visibility = View.GONE
                    setUpPassword()
                }
            } else if (action.equals("FINGER", ignoreCase = true)) {
                mCurrentApp = "app"
                popupView!!.container.visibility = View.GONE
                windowManager!!.updateViewLayout(popupView!!.root, params)


            }


        }

    }

    fun updateDark() {


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground WinMService Channel",
                NotificationManager.IMPORTANCE_LOW
            )

            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            manager?.createNotificationChannel(serviceChannel)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            100, Intent(), 0
        )

        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Xbar")
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle())

            .build()
        startForeground(1, notification)


    }

    private val updateTask: TimerTask = object : TimerTask() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
        override fun run() {

            val intent = Intent("PAKAGENAME")

            if (isConcernedAppIsInForeground()) {
                intent.putExtra("lock", true)

                Log.d("isConcernedAppIsInFrgnd", "true")


            } else {
                mPackageName = " "
                intent.putExtra("lock", false)

                Log.d("isConcernedAppIsInFrgnd", "false")
            }
            applicationContext.sendBroadcast(intent)

        }
    }

    fun setUp() {
        timer = Timer("LockServices")
        timer!!.schedule(updateTask, 0, 500L)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_SPLIT_TOUCH or
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
            PixelFormat.TRANSLUCENT
        )
        params!!.gravity = Gravity.CENTER
        params!!.x = WindowManager.LayoutParams.MATCH_PARENT
        params!!.y = WindowManager.LayoutParams.MATCH_PARENT

        val layoutInflater =
            baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        popupView = ActivityScreenLockBinding.bind(
            layoutInflater.inflate(
                R.layout.activity_screen_lock,
                null
            )
        )

//        try {
        windowManager?.addView(popupView!!.root.rootView, params)
//        } catch (e: Exception) {
//
//        }

//        if (sharedPreferences!!.getBoolean("dark", false)) {
////            popupView!!.container.setBackgroundResource(R.drawable.bg_dark)
//
//        } else {
//            popupView!!.container.setBackgroundResource(R.drawable.bg)
//        }
        windowManager!!.updateViewLayout(popupView!!.root, params)

        if (sharedPreferences.pin == true) {
            popupView!!.llPattern.visibility = View.GONE
            popupView!!.rlPin.visibility = View.VISIBLE
            setUpPin()
        } else {
            popupView!!.llPattern.visibility = View.VISIBLE
            popupView!!.rlPin.visibility = View.GONE
            setUpPassword()
        }
    }

    fun setUpFinger() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (sharedPreferences!!.getBoolean("finger", false)) {
//                fingerprint(applicationContext, true)
//            }
//        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun isConcernedAppIsInForeground(): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val task =
            manager.getRunningTasks(5)
        val usage =
            applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 10000
        var result = ""
        val event = UsageEvents.Event()
        val usageEvents = usage.queryEvents(beginTime, endTime)
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.packageName
            }
        }
        if (!TextUtils.isEmpty(result)) {
            mPackageName = result

        }
//

        for (i in mListAppLock) {
            if (mPackageName != packageName) {
                k++
            }

            if (mPackageName == i.packagename) {
//                    mpackageName = i.packagename
                return true


            }
        }
//            if (mpackageName == "android") {
//                return true
//            }

        Log.d("isConcernedAppIsInFrgnd", mPackageName)
        return false
    }

    @SuppressLint("CheckResult")
    private fun setUpPassword() {

        popupView!!.tvPass.text = resources.getString(R.string.draw_pattern)
        popupView!!.patterLockView.run {
            dotCount = 3
            dotNormalSize =
                com.andrognito.patternlockview.utils.ResourceUtils.getDimensionInPx(
                    this@AppLockerService,
                    com.andrognito.patternlockview.R.dimen.pattern_lock_dot_size
                ).toInt()
            dotSelectedSize =
                com.andrognito.patternlockview.utils.ResourceUtils.getDimensionInPx(
                    this@AppLockerService,
                    com.andrognito.patternlockview.R.dimen.pattern_lock_dot_selected_size
                ).toInt()
            pathWidth =
                com.andrognito.patternlockview.utils.ResourceUtils.getDimensionInPx(
                    this@AppLockerService,
                    com.andrognito.patternlockview.R.dimen.pattern_lock_path_width
                ).toInt()
            isAspectRatioEnabled = true
            aspectRatio =
                PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS
            setViewMode(PatternLockView.PatternViewMode.CORRECT)
            dotAnimationDuration = 150
            pathEndAnimationDuration = 100
//        popupView!!.patter_lock_view.normalStateColor = ResourceUtils.getColor(this,
//            R.color.pattern)
            correctStateColor =
                com.andrognito.patternlockview.utils.ResourceUtils.getColor(
                    this@AppLockerService,
                    R.color.white
                )
//        popupView!!.patter_lock_view.normalStateColor = ResourceUtils.getColor(this,
//            R.color.pattern)
//        if (sharedPreferences!!.getBoolean("dark",false)){
//        popupView!!.patter_lock_view.correctStateColor = ResourceUtils.getColor(this,
//            R.color.white
//        )}else{
//            popupView!!.patter_lock_view.correctStateColor = ResourceUtils.getColor(this,
//                R.color.pattern)
//        }

            isInStealthMode = false
            isTactileFeedbackEnabled = true
            isInputEnabled = true
            addPatternLockListener(mPatternLockViewListener)
//        popupView!!.patter_lock_view.setInStealthMode(true)

//        RxPatternLockView.patternComplete(popupView!!.patter_lock_view)
//            .subscribe(object : Consumer<PatternLockCompleteEvent> {
//                @Throws(Exception::class)
//                override fun accept(patternLockCompleteEvent: PatternLockCompleteEvent) {
//                    Log.d(
//                        javaClass.name,
//                        "Complete: " + patternLockCompleteEvent.pattern.toString()
//                    )
//                }
//            })
//
//        RxPatternLockView.patternChanges(popupView!!.patter_lock_view)
//            .subscribe(object : Consumer<PatternLockCompoundEvent> {
//                @Throws(Exception::class)
//                override fun accept(event: PatternLockCompoundEvent) {
//                    if (event.eventType == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
//                        Log.d(javaClass.name, "Pattern drawing started")
//                    } else if (event.eventType == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
//                        Log.d(
//                            javaClass.name, "Pattern progress: " +
//                                    PatternLockUtils.patternToString(
//                                        popupView!!.patter_lock_view,
//                                        event.pattern
//                                    )
//                        )
//                    } else if (event.eventType == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
//                        Log.d(
//                            javaClass.name, "Pattern complete: " +
//                                    PatternLockUtils.patternToString(
//                                        popupView!!.patter_lock_view,
//                                        event.pattern
//                                    )
//                        )
//                    } else if (event.eventType == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
//                        Log.d(javaClass.name, "Pattern has been cleared")
//                    }
//                }
//            })
        }

    }


    private var mPatternLockViewListener = object : PatternLockViewListener {
        override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {

            if (sharedPreferences.pattern == PatternLockUtils.patternToString(
                    popupView!!.patterLockView,
                    pattern
                )
            ) {
                popupView!!.container.visibility = View.GONE
                mCurrentApp = "app"
                popupView!!.patterLockView.clearPattern()
                windowManager!!.updateViewLayout(popupView!!.root, params)


            } else {
                popupView!!.tvPass.text = resources.getString(R.string.pattern_wrong)

                Handler().postDelayed({
                    popupView!!.patterLockView.clearPattern()
                }, 500)

            }

        }


        override fun onCleared() {
        }

        override fun onStarted() {
        }

        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
            popupView!!.tvPass.text = resources.getString(R.string.draw_pattern)
        }

    }

    private fun setUpPin() {

        popupView!!.tvPassPin.text = resources.getString(R.string.enter_pin)
        popupView!!.pinLockViewPin.run {
            attachIndicatorDots(popupView!!.indicatorDotsPin)
            setPinLockListener(mPinLockListener)
            pinLength = 4
            textColor = ContextCompat.getColor(this@AppLockerService, R.color.white)

        }

        popupView!!.indicatorDotsPin.indicatorType =
            IndicatorDots.IndicatorType.FILL_WITH_ANIMATION
    }

    private val mPinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            if (sharedPreferences.pattern == pin

            ) {

                popupView!!.container.visibility = View.GONE
                mCurrentApp = "app"
                popupView!!.pinLockViewPin.resetPinLockView()
                windowManager!!.updateViewLayout(popupView!!.root, params)

            } else {
                popupView!!.tvPassPin.text = resources.getString(R.string.pin_wrong)
                Handler().postDelayed({
                    popupView!!.pinLockViewPin.resetPinLockView()
                }, 500)

            }
        }

        override fun onEmpty() {
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            popupView!!.tvPassPin.text = resources.getString(R.string.enter_pin)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val keyGenerator: KeyGenerator
        keyGenerator = try {
            KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        } catch (e: NoSuchProviderException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }
        try {
            keyStore!!.load(null)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or
                            KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
                    )
                    .build()
            )
            keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    fun cipherInit(): Boolean {
        cipher = try {
            Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }
        return try {
            keyStore!!.load(null)
            val key = keyStore!!.getKey(
                KEY_NAME,
                null
            ) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e: Exception) {
            false
        }
    }


}

