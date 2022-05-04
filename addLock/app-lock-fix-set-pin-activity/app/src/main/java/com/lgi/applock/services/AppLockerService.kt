package com.lgi.applock.services

import android.annotation.SuppressLint
import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.*
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.lgi.applock.R
import com.lgi.applock.data.db.DbApp
import com.bumptech.glide.Glide
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher

import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.lgi.applock.databinding.ActivityScreenLockBinding
import com.lgi.applock.layouts.PinInputKeyboard
import com.lgi.applock.models.DBAppLock
import com.lgi.applock.models.LockState
import com.lgi.applock.models.TypeActionUpdate
import com.lgi.applock.models.UpdateEvent
import com.lgi.applock.utils.preferences.LockPreferences
import com.poovam.pinedittextfield.PinField
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class AppLockerService : Service() {
    var keyStore: KeyStore? = null

    @Inject
    lateinit var sharedPreferences: LockPreferences

    // Variable used for storing the key in the Android Keystore container
    val KEY_NAME = "androidHive"
    var cipher: Cipher? = null
    var params: WindowManager.LayoutParams? = null
    val CHANNEL_ID = "com.lgi.applock"
    var manager: NotificationManager? = null
    var notification: Notification? = null
    var windowManager: WindowManager? = null
    var mListAppLock = ArrayList<DBAppLock>()
    var dbApp: DbApp? = null
    var pinCode = ""
    private var finalCode = ""
    var timer: Timer? = null
    lateinit var popupView: ActivityScreenLockBinding

    companion object {
        var mCurrentApp = ""
        var mPackageName = ""
        var inApp = true
        var i = 0
        var k = 0

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    override fun onCreate() {
        setUp()
        EventBus.getDefault().register(this)
        super.onCreate()
        sharedPreferences = LockPreferences()
        dbApp = DbApp(applicationContext, null)

        mListAppLock = dbApp!!.getLocked()

        if (mListAppLock.size == 0) {
            mListAppLock.add(DBAppLock("App Lock", "com.lgi.applock", 1, 0, 0))
        }

        val intent = IntentFilter()
        intent.addAction("LOCKED")
        intent.addAction("LOCKEDAPP")
        intent.addAction("PAKAGENAME")
        applicationContext.registerReceiver(broadcastReceiver, intent)

    }

    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1?.action

            if (action!!.equals("PAKAGENAME", ignoreCase = true)) {

                val lock = p1.extras?.getBoolean("lock")
                if (lock!!) {
                    /// show activity lock

                    if (mCurrentApp != "app") {
                        if (i == 0) {
                            i = 1
                        }
                        if (k > 0) {
                            popupView.container.visibility = View.VISIBLE
                            windowManager!!.updateViewLayout(popupView.root, params)
                        }
//
                    }


                } else {
                    i = 0
                    mCurrentApp = ""
                    popupView.container.visibility = View.GONE
                    windowManager!!.updateViewLayout(popupView.root, params)

                }
            }

        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("onStartCommand", intent?.getStringExtra("inApp").toString())
        mCurrentApp = if (intent?.hasExtra("inApp") != null) "app" else ""
        if (sharedPreferences.lockPin == true) {
            popupView.llPattern.visibility = View.GONE
            popupView.rlPin.visibility = View.VISIBLE
            setUpPin()
        } else {
            popupView.llPattern.visibility = View.VISIBLE
            popupView.rlPin.visibility = View.GONE
            setUpPassword()
        }
        timer = Timer("LockServices")
        timer!!.schedule(updateTask, 0, 300L)
        createNotificationChannel()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        timer!!.cancel()
        timer = null
        windowManager!!.removeViewImmediate(popupView.root)
        windowManager = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateFavoriteEvent(event: UpdateEvent) {
        Log.e("onUpdateFavoriteEvent", "onUpdateFavoriteEvent ${event.isUpdate}")
        if (event.isUpdate == TypeActionUpdate.Lock) {
            mListAppLock = dbApp!!.getLocked()
        }

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
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                100, Intent(), PendingIntent.FLAG_IMMUTABLE,
            )
        } else {
            PendingIntent.getActivity(
                this,
                100, Intent(), 0,
            )
        }

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
                mPackageName = ""
                intent.putExtra("lock", false)

                Log.d("isConcernedAppIsInFrgnd", "false")
            }
            applicationContext.sendBroadcast(intent)

        }
    }

    private fun setUp() {
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

        popupView.container.visibility = View.GONE
        windowManager?.addView(popupView.root.rootView, params)
        windowManager?.updateViewLayout(popupView.root, params)
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
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED || event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
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
                return true
            }
        }

        Log.d("isConcernedAppIsInFrgnd", mPackageName)
        return false
    }

    @SuppressLint("CheckResult")
    private fun setUpPassword() {

        popupView.tvPass.text = resources.getString(R.string.draw_pattern)
        popupView.patterLockView.run {
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
            correctStateColor =
                com.andrognito.patternlockview.utils.ResourceUtils.getColor(
                    this@AppLockerService,
                    R.color.white
                )
//

            isInStealthMode = false
            isTactileFeedbackEnabled = true
            isInputEnabled = true
            addPatternLockListener(mPatternLockViewListener)
        }

    }


    private var mPatternLockViewListener = object : PatternLockViewListener {
        override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {

            if (sharedPreferences.password == PatternLockUtils.patternToString(
                    popupView.patterLockView,
                    pattern
                )
            ) {
                popupView.container.visibility = View.GONE
                mCurrentApp = "app"
                popupView.patterLockView.clearPattern()
                windowManager!!.updateViewLayout(popupView.root, params)


            } else {
                popupView.tvPass.text = resources.getString(R.string.pattern_wrong)

                Handler().postDelayed({
                    popupView.patterLockView.clearPattern()
                }, 500)

            }

        }


        override fun onCleared() {
        }

        override fun onStarted() {
        }

        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
            popupView.tvPass.text = resources.getString(R.string.draw_pattern)
        }

    }

    private fun setUpPin() {
        popupView.tvPassPin.text = resources.getString(R.string.enter_pin)
        popupView.inputField.showSoftInputOnFocus = false
        popupView.loutPinInputKeyboard.onPressPinInputKeyListener = object :
            PinInputKeyboard.OnPressPinInputKeyListener {
            override fun onClickKey(keyString: String) {
                if (popupView.inputField.text.toString().length < 4) {
                    pinCode += keyString
                    popupView.inputField.setText(pinCode)
                }
            }

            override fun onClickDelete() {
                if (pinCode == "") {
                    return
                } else {
                    pinCode = pinCode.substring(0, pinCode.length - 1)
                }
                popupView.inputField.setText(pinCode)
            }
        }

        popupView.inputField.onTextCompleteListener = object : PinField.OnTextCompleteListener {
            override fun onTextComplete(enteredText: String): Boolean {
                if (enteredText == sharedPreferences.password) {
                    mCurrentApp = "app"
                    pinCode = ""
                    popupView.inputField.setText(pinCode)
                    popupView.container.visibility = View.GONE
                    windowManager!!.updateViewLayout(popupView.root, params)
                }
                return true
            }

        }
    }


}

