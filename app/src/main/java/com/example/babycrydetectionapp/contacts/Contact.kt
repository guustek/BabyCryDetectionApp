package com.example.babycrydetectionapp.contacts

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(

    val name: String,
    @PrimaryKey
    val number: String,
    //val image: Bitmap

    )