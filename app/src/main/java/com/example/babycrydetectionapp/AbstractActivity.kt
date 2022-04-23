package com.example.babycrydetectionapp

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

abstract class AbstractActivity : AppCompatActivity(){

     open lateinit var preferences: SharedPreferences

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         preferences = PreferenceManager.getDefaultSharedPreferences(this)
     }
 }