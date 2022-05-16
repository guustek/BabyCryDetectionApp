package com.example.babycrydetectionapp

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference


class SettingsActivity : AbstractActivity() {
    companion object {
        private const val DO_NOT_DISTURB_REQUEST_CODE = 12345
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "Settings"
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        private val notificationManager by lazy { requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
        private lateinit var preferences: SharedPreferences

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        }

        override fun onSharedPreferenceChanged(p0: SharedPreferences, p1: String?) {
            if (p1 == "mute_phone" && p0.getBoolean(p1, false))
                checkForMutePermissions()
        }

        private fun checkForMutePermissions() {
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                preferenceManager.findPreference<SwitchPreference>("mute_phone")?.isChecked = false
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivityForResult(intent, DO_NOT_DISTURB_REQUEST_CODE)
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == DO_NOT_DISTURB_REQUEST_CODE && notificationManager.isNotificationPolicyAccessGranted) {
                preferenceManager.findPreference<SwitchPreference>("mute_phone")?.isChecked = true
            }
        }

        override fun onResume() {
            super.onResume()
            preferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            preferences.unregisterOnSharedPreferenceChangeListener(this)
            super.onPause()
        }
    }
}