package com.example.babycrydetectionapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babycrydetectionapp.databinding.ActivityMainBinding
import org.tensorflow.lite.task.audio.classifier.AudioClassifier

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_RECORD_AUDIO = 2137
        private const val MODEL_FILE = "yamnet.tflite"
        private const val MINIMUM_DISPLAY_THRESHOLD = 0.3f
    }

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

        requestMicrophonePermission()
        audioRecord = audioClassifier.createAudioRecord()
        audioRecord.startRecording()

        val handlerThread = HandlerThread("classificationThread")
        handlerThread.start()
        handler = HandlerCompat.createAsync(handlerThread.looper)

        val classification = Runnable {
            val audioTensor = audioClassifier.createInputTensorAudio()
            audioTensor.load(audioRecord)
            val output = audioClassifier.classify(audioTensor)
            val filteredModelOutput = output[0].categories.filter {
                it.score > MINIMUM_DISPLAY_THRESHOLD
            }.sortedBy {
                -it.score
            }

            runOnUiThread {
                probabilitiesAdapter.categoryList = filteredModelOutput
                probabilitiesAdapter.notifyDataSetChanged()
            }
        }

        binding.classifyButton.setOnClickListener {
            handler.post(classification)
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
}