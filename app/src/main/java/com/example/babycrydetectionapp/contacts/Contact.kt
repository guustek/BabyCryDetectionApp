package com.example.babycrydetectionapp.contacts

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(
    val name: String,
    val number: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)