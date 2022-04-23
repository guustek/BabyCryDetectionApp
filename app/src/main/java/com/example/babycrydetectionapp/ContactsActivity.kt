package com.example.babycrydetectionapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycrydetectionapp.databinding.ActivityMainBinding
import com.example.babycrydetectionapp.databinding.ContactsActivityBinding

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ContactsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.contactsRecyclerView.adapter = ContactsAdapter()
    }
}