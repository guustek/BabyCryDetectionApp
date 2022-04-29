package com.example.babycrydetectionapp

import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babycrydetectionapp.databinding.ContactsActivityBinding


class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ContactsActivityBinding
    private var list = mutableListOf(Contact("Emulator", "5544", null), Contact("Paweł", "+48512013073", null))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                        .setView(R.layout.add_contact_dialog)
                        .create()
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.add)) { _: DialogInterface, _: Int ->
                        val name = dialog.findViewById<EditText>(R.id.name_input)!!.text.toString()
                        val number = dialog.findViewById<EditText>(R.id.number_input)!!.text.toString()
                        val newContact = Contact(name, number,null)
                        list.add(newContact)
                        (binding.contactsRecyclerView.adapter as ContactsAdapter).notifyNewContactAdded()
                    }
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.cancel)) { _: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    dialog.show()
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
            adapter = ContactsAdapter(list)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
}