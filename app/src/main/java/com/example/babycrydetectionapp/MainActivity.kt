package com.example.babycrydetectionapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.SystemClock
import android.telephony.PhoneNumberUtils
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babycrydetectionapp.databinding.ActivityMainBinding
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_RECORD_AUDIO = 2137
        const val REQUEST_SEND_SMS = 2115
        private const val MODEL_FILE = "yamnet.tflite"
        private const val MINIMUM_DISPLAY_THRESHOLD = 0.3f
    }

    private var classificationInterval = 500L

    private lateinit var binding: ActivityMainBinding
    private val probabilitiesAdapter by lazy { ProbabilitiesAdapter() }

    private val audioClassifier: AudioClassifier by lazy { AudioClassifier.createFromFile(this, MODEL_FILE) }
    private lateinit var audioRecord: AudioRecord
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            probabilitiesList.apply {
                adapter = probabilitiesAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        // zażądanie pozwolenia na nagrywanie audio
        requestMicrophonePermission()
        requestSmsSendingPermission()
        // utworzenie audio rekordera na podstawie formatu wymaganego przez klasyfikator
        audioRecord = audioClassifier.createAudioRecord()

        // utworzenie handlera oddzielnego wątku klasyfikacji
        val handlerThread = HandlerThread("classificationThread")
        handlerThread.start()
        handler = HandlerCompat.createAsync(handlerThread.looper)

        binding.classifyButton.setOnClickListener {
            startListening()
        }
    }

    private fun startListening() {
        binding.timer.start()
        binding.classifyButton.setImageResource(R.drawable.rectangle_48dp);
        binding.classifyButton.setOnClickListener {
            stopListening()
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
                //Baby cry, infant cry
                if (filteredOutput.any { it.label == "Baby cry, infant cry" }) {
                    runOnUiThread { stopListening() }
                    val smsManager = SmsManager.getDefault() as SmsManager

                    val number =
                        PhoneNumberUtils.formatNumber(binding.phoneNumber.text.toString(), Locale.getDefault().country)
                    //5554 to numer mojego emulatora u was może być inaczej
                    smsManager.sendTextMessage(
                        binding.phoneNumber.text.toString(),
                        null,
                        "Wykryto płacz dziecka!",
                        null,
                        null
                    )
                } else
                // Rerun the classification after a certain interval if didnt find searched sound class
                    handler.postDelayed(this, classificationInterval)

                runOnUiThread {
                    probabilitiesAdapter.categoryList = filteredOutput
                    probabilitiesAdapter.notifyDataSetChanged()
                }
                val end = System.nanoTime()
                Log.d("Classification","Elapsed time in seconds: ${(end-begin)/1000000}")
            }

        }
        // rozpoczęcie nasłuchiwania
        audioRecord.startRecording()
        handler.post(classification)
    }

    private fun stopListening() {
        audioRecord.stop()
        binding.timer.base = SystemClock.elapsedRealtime()
        binding.timer.stop()
        binding.classifyButton.setImageResource(R.drawable.micro_48dp);
        handler.removeCallbacksAndMessages(null)
        binding.classifyButton.setOnClickListener {
            startListening()
        }
    }

    private fun requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO)
        }
    }

    private fun requestSmsSendingPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), REQUEST_SEND_SMS)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}