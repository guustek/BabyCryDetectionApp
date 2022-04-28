package com.example.babycrydetectionapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.AudioRecord
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.HandlerCompat
import androidx.preference.PreferenceManager
import org.tensorflow.lite.task.audio.classifier.AudioClassifier


class ClassificationService : Service() {

    companion object {
        private const val MODEL_FILE = "yamnet.tflite"
        private const val MINIMUM_DISPLAY_THRESHOLD = 0.3f
        private const val CLASSIFICATION_INTERVAL = 500L
        const val RESULT_BROADCAST = "XD"
        const val FINISHED_BROADCAST = "JP2"
        private const val NOTIFICATION_CHANNEL_ID = "com.example.babycrydetectionapp"
        private const val SERVICE_ID = 420
    }

    private lateinit var handlerThread: HandlerThread
    private lateinit var preferences: SharedPreferences
    private val audioClassifier: AudioClassifier by lazy { AudioClassifier.createFromFile(this, MODEL_FILE) }
    private lateinit var audioRecord: AudioRecord
    private lateinit var handler: Handler


    override fun onCreate() {
        super.onCreate()

        // utworzenie audio rekordera na podstawie formatu wymaganego przez klasyfikator
        audioRecord = audioClassifier.createAudioRecord()
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        setupNotificationChannel()

        // utworzenie handlera oddzielnego wątku klasyfikacji
        handlerThread = HandlerThread("classificationThread")
        handlerThread.start()
        handler = HandlerCompat.createAsync(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startListening()
        startForeground(SERVICE_ID, createNotification())
        return START_REDELIVER_INTENT
    }

    private fun startListening() {
        if (preferences.getBoolean("mute_phone", false)) {
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
        val classification = object : Runnable {
            override fun run() {
                val begin = System.nanoTime()
                // tworzy obiekt przechowujący dane do klasyfikacji
                val tensorAudio = audioClassifier.createInputTensorAudio()
                // ładuje próbkę dźwięku
                tensorAudio.load(audioRecord)
                // klasyfikacja
                val output = audioClassifier.classify(tensorAudio)
                // filtrowanie (tylko kategorie z prawdopodobieństwem > 0.3)
                val filteredOutput = output[0].categories.filter {
                    it.score > MINIMUM_DISPLAY_THRESHOLD
                }.sortedBy {
                    -it.score
                }

                val wrappedOutput = ArrayList(filteredOutput.map { category -> CategoryAdapter(category) })
                val broadcastIntent = Intent(RESULT_BROADCAST)
                broadcastIntent.putParcelableArrayListExtra(RESULT_BROADCAST, wrappedOutput)
                sendBroadcast(broadcastIntent)

                //Baby cry, infant cry
                if (filteredOutput.any { it.label == "Speech" }) {
                    Log.d("Classification", "Detected!!")
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(
                        "+48512013073",
                        null,
                        preferences.getString("detection_message_text", getString(R.string.detection_default_message)),
                        null,
                        null
                    )
                    sendBroadcast(Intent(FINISHED_BROADCAST))
                } else
                    handler.postDelayed(this, CLASSIFICATION_INTERVAL)

                val end = System.nanoTime()
                Log.d("Classification", "Elapsed time in milliseconds: ${(end - begin) / 1000000}")
            }
        }
        // rozpoczęcie nasłuchiwania
        audioRecord.startRecording()
        handler.post(classification)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecord.stop()
        handlerThread.quit()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "${getString(R.string.app_name)} notification channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(true)
            channel.lightColor = R.color.primaryColor
            channel.enableVibration(false)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val openPendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val stopIntent = Intent(FINISHED_BROADCAST)
        val stopPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(getString(R.string.notification_content_title))
            .setSmallIcon(R.drawable.bobo)
            .setContentIntent(openPendingIntent)
            .addAction(0, getString(R.string.stop), stopPendingIntent)
            .build()
    }
}