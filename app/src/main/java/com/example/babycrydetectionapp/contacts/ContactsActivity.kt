package com.example.babycrydetectionapp.contacts

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babycrydetectionapp.R
import com.example.babycrydetectionapp.databinding.ContactsActivityBinding
import com.hbb20.CountryCodePicker


class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ContactsActivityBinding
    private val contactsViewModel: ContactsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactsViewModel.contacts.value =
            ContactDatabase.getDatabase(applicationContext).contactDao().getAllContacts()

        setupRecyclerView()
        setupToolbarMenu()
    }

    private fun setupFilterOption() {
        (binding.contactsToolbar.menu.findItem(R.id.contacts_menu_search).actionView as SearchView).apply {
            maxWidth = 770
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    (binding.contactsRecyclerView.adapter as ContactsAdapter).filter.filter(s)
                    return true
                }
            })
        }
    }

    private fun setupToolbarMenu() {
        binding.contactsToolbar.inflateMenu(R.menu.contacts_menu)
        binding.contactsToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.contacts_menu_add -> {
                    val dialog = AlertDialog.Builder(this)
                        .setTitle(getString(R.string.add_contact))
                        .setCancelable(true)
                        .setView(R.layout.contact_dialog)
                        .create()
                    dialog.setButton(
                        AlertDialog.BUTTON_POSITIVE,
                        getString(R.string.add)
                    ) { _: DialogInterface, _: Int -> }
                    dialog.setButton(
                        AlertDialog.BUTTON_NEGATIVE,
                        getString(R.string.cancel)
                    ) { _: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    dialog.show()
                    val nameInput = dialog.findViewById<EditText>(R.id.name_input)!!
                    val numberInput = dialog.findViewById<EditText>(R.id.number_input)!!
                    val countryInput = dialog.findViewById<CountryCodePicker>(R.id.country_input)!!
                    countryInput.registerCarrierNumberEditText(numberInput)
                    countryInput.setNumberAutoFormattingEnabled(true)
                    nameInput.addTextChangedListener {
                        nameInput.error=null
                        if (nameInput.text.isEmpty())
                            nameInput.error = getString(R.string.name_is_empty)
                    }
                    numberInput.addTextChangedListener {
                        numberInput.error = null
                        if(!countryInput.isValidFullNumber)
                           numberInput.error = getString(R.string.invalid_number)
                    }

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        if (nameInput.error == null && numberInput.error == null) {
                            val newContact = Contact(nameInput.text.toString(), countryInput.formattedFullNumber)
                            if(!contactsViewModel.contacts.value!!.contains(newContact)) {
                                ContactDatabase.getDatabase(applicationContext).contactDao().addContact(newContact)
                                contactsViewModel.contacts.value = contactsViewModel.contacts.value!!.plus(newContact)
                                Log.d("XD", contactsViewModel.contacts.value.toString())
                            }else
                                Toast.makeText(applicationContext,getString(R.string.number_exists),Toast.LENGTH_LONG).show()
                            val adapter = binding.contactsRecyclerView.adapter as ContactsAdapter
                            adapter.notifyItemInserted(adapter.itemCount)
                            adapter.refresh()
                            dialog.dismiss()
                        }
                    }
                    true
                }
                R.id.contacts_menu_import -> {
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }
        setupFilterOption()
    }

    private fun setupRecyclerView() {
        binding.contactsRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = ContactsAdapter(contactsViewModel, context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
}