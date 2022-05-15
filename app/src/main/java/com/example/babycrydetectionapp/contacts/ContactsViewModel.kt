package com.example.babycrydetectionapp.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Previous view model

class ContactsViewModel : ViewModel() {
    val contacts: MutableLiveData<List<Contact>> by lazy { MutableLiveData() }
}