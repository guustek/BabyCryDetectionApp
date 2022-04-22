package com.example.babycrydetectionapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

abstract class AbstractActivity : AppCompatActivity(){

     open lateinit var preferences: SharedPreferences

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         preferences = PreferenceManager.getDefaultSharedPreferences(this)
     }
 }