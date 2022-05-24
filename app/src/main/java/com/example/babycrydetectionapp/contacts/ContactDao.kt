package com.example.babycrydetectionapp.contacts

import androidx.room.*

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addContact(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addContacts(contacts: List<Contact>)

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    fun getAllContacts(): List<Contact>

    @Delete
    fun deleteContact(contact: Contact)

//    @Update
//    fun editContact(contact: Contact)
}