package com.example.babycrydetectionapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babycrydetectionapp.databinding.ActivityMainBinding


class MainActivity : AbstractActivity() {

    companion object {
        const val REQUEST_RECORD_AUDIO = 2137
        const val REQUEST_SEND_SMS = 2115
    }


    private lateinit var binding: ActivityMainBinding
    private val probabilitiesAdapter by lazy { ProbabilitiesAdapter() }
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(applicationContext, ClassificationService::class.java)
        val intentFilter = IntentFilter()
        intentFilter.addAction(ClassificationService.CLASSIFICATION_RESULT)
        intentFilter.addAction(ClassificationService.SERVICE_FINISHED)
        registerReceiver(classificationServiceReceiver, intentFilter)



        with(binding) {
            probabilitiesList.apply {
                adapter = probabilitiesAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
        // zażądanie pozwolenia na nagrywanie audio
        requestMicrophonePermission()
        requestSmsSendingPermission()

        binding.classifyButton.setOnClickListener {
            startListening()
        }

        binding.button.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.menu_tutorial -> startActivity(Intent(this,TutorialActivity::class.java))
//                R.id.menu_contacts -> startActivity(Intent(this,ContactsActivity::class.java))
                R.id.menu_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            false
        }
        binding.navView.bringToFront()
    }

    override fun onResume() {
        super.onResume()
        binding.probabilitiesList.visibility =
            if (preferences.getBoolean("display_results", true)) View.VISIBLE else View.INVISIBLE

        binding.timer.visibility =
            if (preferences.getBoolean("display_timer", true)) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, ClassificationService::class.java))
        unregisterReceiver(classificationServiceReceiver)
    }

    private fun startListening() {
        binding.timer.base = SystemClock.elapsedRealtime()
        binding.timer.start()
        applicationContext.startService(Intent(this, ClassificationService::class.java))
        binding.classifyButton.setImageResource(R.drawable.rectangle_48dp)
        binding.classifyButton.setOnClickListener { stopListening() }
    }

    private fun stopListening() {
        binding.timer.stop()
        binding.timer.base = SystemClock.elapsedRealtime()
        applicationContext.stopService(Intent(this, ClassificationService::class.java))
        binding.classifyButton.setImageResource(R.drawable.micro_48dp)
        binding.classifyButton.setOnClickListener { startListening() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val classificationServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ClassificationService.CLASSIFICATION_RESULT -> {
                    val parcel =
                        intent.getParcelableArrayListExtra<CategoryAdapter>(ClassificationService.CLASSIFICATION_RESULT)
                    val categories = parcel!!.map { it.wrappedCategory() }
                    runOnUiThread {
                        probabilitiesAdapter.categoryList = categories
                        probabilitiesAdapter.notifyDataSetChanged()
                    }
                }
                ClassificationService.SERVICE_FINISHED -> stopListening()
            }
        }
    }

    private fun requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permissions", "Microphone permission granted")
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
            Log.d("Permissions", "Sms sending permission granted")
        } else {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), REQUEST_SEND_SMS)
        }
    }


}