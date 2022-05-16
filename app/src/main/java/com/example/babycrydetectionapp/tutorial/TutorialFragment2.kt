package com.example.babycrydetectionapp.tutorial

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycrydetectionapp.R
import com.example.babycrydetectionapp.contacts.Contact
import com.example.babycrydetectionapp.databinding.ContactsActivityBinding
import com.example.babycrydetectionapp.databinding.ContactsItemViewBinding
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity

class TutorialFragment2 : Fragment() {
    private lateinit var addGuide: GuideView
    private lateinit var importGuide: GuideView
    private lateinit var searchGuide: GuideView

    private var _binding: ContactsActivityBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupToolbarMenu()

    }

    private fun setupToolbarMenu() {
        binding.contactsToolbar.inflateMenu(R.menu.contacts_menu)
        val xd = binding.contactsToolbar.menu.findItem(R.id.contacts_menu_search)
        xd.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return true
            }
        })
        addGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.add_button))
            .setContentText(getString(R.string.add_button_description))
            .setGravity(Gravity.auto) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView(view!!.findViewById(R.id.contacts_menu_add))
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .build()

        importGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.import_button))
            .setContentText(getString(R.string.import_button_description))
            .setGravity(Gravity.auto) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView(view!!.findViewById(R.id.contacts_menu_import))
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .build()

        searchGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.search_button))
            .setContentText(getString(R.string.search_button_description))
            .setGravity(Gravity.auto) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView(view!!.findViewById(R.id.contacts_menu_search))
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .build()

        addGuide.show()
        addGuide.dismiss()

        importGuide.show()
        importGuide.dismiss()

        searchGuide.show()
        searchGuide.dismiss()

        binding.contactsToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.contacts_menu_add -> {
                    addGuide.show()
                    true
                }
                R.id.contacts_menu_import -> {
                    importGuide.show()
                    true
                }
                R.id.contacts_menu_search -> {
                    searchGuide.show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.contactsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ContactsTutorialAdapter(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ContactsTutorialAdapter(private val context: Context) :
    RecyclerView.Adapter<ContactsTutorialAdapter.ContactTutorialHolder>() {

    private lateinit var binding: ContactsItemViewBinding
    private var contacts: List<Contact> = listOf(Contact("Jan Kowalski", "+48 542 678 909"))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsTutorialAdapter.ContactTutorialHolder {
        binding = ContactsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactTutorialHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ContactTutorialHolder, position: Int) {
        holder.bind(contacts[position], position)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ContactTutorialHolder(private val binding: ContactsItemViewBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, position: Int) {
            binding.contactName.text = contact.name
            binding.contactNumber.text = contact.number
            binding.root.setOnClickListener {
                GuideView.Builder(context)
                    .setTitle(context.getString(R.string.saved_contact))
                    .setContentText(context.getString(R.string.contact_info))
                    .setGravity(Gravity.auto) //optional
                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                    .setTargetView(it)
                    .setContentTextSize(12) //optional
                    .setTitleTextSize(14) //optional
                    .build()
                    .show()
            }
            binding.deleteButton.setOnClickListener {
                GuideView.Builder(context)
                    .setTitle(context.getString(R.string.delete_button))
                    .setContentText(context.getString(R.string.delete_button_description))
                    .setGravity(Gravity.auto) //optional
                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                    .setTargetView(it)
                    .setContentTextSize(12) //optional
                    .setTitleTextSize(14) //optional
                    .build()
                    .show()
            }
        }

    }

}


