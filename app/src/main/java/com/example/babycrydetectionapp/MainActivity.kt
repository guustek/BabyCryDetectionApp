package com.example.babycrydetectionapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.ContactsContract.Contacts
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babycrydetectionapp.contacts.Contact
import com.example.babycrydetectionapp.contacts.ContactDatabase
import com.example.babycrydetectionapp.contacts.ContactsActivity
import com.example.babycrydetectionapp.databinding.ActivityMainBinding
import com.example.babycrydetectionapp.tutorial.TutorialActivity


class MainActivity : AbstractActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 2137
    }

    private lateinit var binding: ActivityMainBinding
    private val probabilitiesAdapter by lazy { ProbabilitiesAdapter() }
    private lateinit var serviceIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!ContactDatabase.getDatabase(this).contactDao().getAllContacts().contains(Contact("Emulator", "5554")))
            ContactDatabase.getDatabase(this).contactDao().addContact(Contact("Emulator", "5554"))

        serviceIntent = Intent(applicationContext, ClassificationService::class.java)
        val intentFilter = IntentFilter()
        intentFilter.addAction(ClassificationService.RESULT_BROADCAST)
        intentFilter.addAction(ClassificationService.FINISHED_BROADCAST)
        registerReceiver(classificationServiceReceiver, intentFilter)

        with(binding) {
            probabilitiesList.apply {
                adapter = probabilitiesAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        checkPermissions()

        binding.classifyButton.setOnClickListener {
            startListening()
        }

        binding.button.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tutorial -> startActivity(Intent(this, TutorialActivity::class.java))
                R.id.menu_contacts -> startActivity(Intent(this, ContactsActivity::class.java))
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
        ContextCompat.startForegroundService(this, Intent(this, ClassificationService::class.java))
        binding.classifyButton.setImageResource(R.drawable.rectangle_48dp)
        binding.classifyButton.setOnClickListener { stopListening() }
    }

    private fun stopListening() {
        binding.timer.stop()
        binding.timer.base = SystemClock.elapsedRealtime()
        stopService(Intent(this, ClassificationService::class.java))
        binding.classifyButton.setImageResource(R.drawable.micro_48dp)
        binding.classifyButton.setOnClickListener { startListening() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val classificationServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ClassificationService.RESULT_BROADCAST -> {
                    val parcel =
                        intent.getParcelableArrayListExtra<CategoryWrapper>(ClassificationService.RESULT_BROADCAST)
                    val categories = parcel!!.map { it.wrappedCategory() }
                    runOnUiThread {
                        probabilitiesAdapter.categoryList = categories
                        probabilitiesAdapter.notifyDataSetChanged()
                    }
                }
                ClassificationService.FINISHED_BROADCAST -> stopListening()
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED
        )
            Log.d("Permissions", "Permissions already granted")
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.SEND_SMS),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
//                TODO("Co jak madka nie pozwoli na nagrywanie dźwięku i sms")
//            }
//        }
    }
}