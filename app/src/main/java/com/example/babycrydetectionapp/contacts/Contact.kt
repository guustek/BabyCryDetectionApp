package com.example.babycrydetectionapp.contacts

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Contact(

    val name: String,
    val number: String,
    val image: Drawable?

    )