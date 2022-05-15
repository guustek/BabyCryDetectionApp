package com.example.babycrydetectionapp.contacts

import androidx.room.*

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addContact(contact: Contact)

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    fun getAllContacts(): List<Contact>

    @Delete
    fun deleteContact(contact: Contact)

//    @Update
//    fun editContact(contact: Contact)
}