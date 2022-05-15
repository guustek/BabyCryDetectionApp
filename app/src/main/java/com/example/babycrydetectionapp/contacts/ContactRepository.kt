package com.example.babycrydetectionapp.contacts

import androidx.lifecycle.LiveData

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    suspend fun addContact(contact: Contact) {
        contactDao.addContact(contact)
    }

}