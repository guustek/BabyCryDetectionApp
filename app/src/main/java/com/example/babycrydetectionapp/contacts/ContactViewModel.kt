package com.example.babycrydetectionapp.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// New view model for RoomDB

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    val allContacts : LiveData<List<Contact>>
    private val repository : ContactRepository

    init {
        val contactDao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(contactDao)
        allContacts = repository.allContacts
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addContact(contact)
        }
    }

}