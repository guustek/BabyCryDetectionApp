package com.example.babycrydetectionapp.contacts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        private var instance: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    context,
                    ContactDatabase::class.java,
                    "contact_database"
                )
                    .allowMainThreadQueries()
                    .build()
            return instance!!
        }
    }
}