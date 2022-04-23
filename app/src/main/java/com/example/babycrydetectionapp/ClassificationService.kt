package com.example.babycrydetectionapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioRecord
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.os.HandlerCompat
import org.tensorflow.lite.task.audio.classifier.AudioClassifier


class ClassificationService : Service() {

    companion object {
        private const val MODEL_FILE = "yamnet.tflite"
        private const val MINIMUM_DISPLAY_THRESHOLD = 0.3f
        private const val CLASSIFICATION_INTERVAL = 500L
        const val CLASSIFICATION_RESULT = "XD"
        const val SERVICE_FINISHED = "JP2"
    }

    private val audioClassifier: AudioClassifier by lazy { AudioClassifier.createFromFile(this, MODEL_FILE) }
    private lateinit var audioRecord: AudioRecord
    private lateinit var handler: Handler
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val channelId = "com.example.babycrydetectionapp"


    override fun onCreate() {
        super.onCreate()
        // utworzenie audio rekordera na podstawie formatu wymaganego przez klasyfikator
        audioRecord = audioClassifier.createAudioRecord()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotification()
        // utworzenie handlera oddzielnego wątku klasyfikacji
        val handlerThread = HandlerThread("classificationThread")
        handlerThread.start()
        handler = HandlerCompat.createAsync(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startListening()
        return START_REDELIVER_INTENT

    }

    private fun startListening() {
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
                val broadcastIntent = Intent(CLASSIFICATION_RESULT)
                broadcastIntent.putParcelableArrayListExtra(CLASSIFICATION_RESULT, wrappedOutput)
                sendBroadcast(broadcastIntent)

                //Baby cry, infant cry
                if (filteredOutput.any { it.label == "Speech" }) {
                    Log.d("Classification", "Detected!!")
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage("5554", null, "ES", null, null)
                    stopListening()
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

    private fun stopListening() {
        sendBroadcast(Intent(SERVICE_FINISHED))
        stopSelf()
    }


    override fun onDestroy() {
        super.onDestroy()
        audioRecord.stop()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,"pizda",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.CYAN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("balls")
                .setContentText("cum")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.mipmap.ic_launcher))
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(123,notificationBuilder.build())
        }else Toast.makeText(this,"api small", Toast.LENGTH_LONG).show()
        return
    }
}