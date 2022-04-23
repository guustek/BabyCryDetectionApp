package com.example.babycrydetectionapp

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.AudioRecord
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
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

    override fun onCreate() {
        super.onCreate()
        // utworzenie audio rekordera na podstawie formatu wymaganego przez klasyfikator
        audioRecord = audioClassifier.createAudioRecord()
        // utworzenie handlera oddzielnego wątku klasyfikacji
        val handlerThread = HandlerThread("classificationThread")
        handlerThread.start()
        handler = HandlerCompat.createAsync(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startListening()
        return START_REDELIVER_INTENT

    }

    private fun createNotification(): Notification {
        TODO("Powiadomienie o działającym serwisie")
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
}