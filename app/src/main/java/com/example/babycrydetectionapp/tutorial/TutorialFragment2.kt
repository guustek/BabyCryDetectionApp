package com.example.babycrydetectionapp.tutorial

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.babycrydetectionapp.R
import com.example.babycrydetectionapp.contacts.Contact
import com.example.babycrydetectionapp.contacts.ContactsAdapter
import com.example.babycrydetectionapp.contacts.JakisGownoSingletonDoPrzekazaniaNumerowDoSerwisuBoNieChceMiSieRobicBazyDanych
import com.example.babycrydetectionapp.databinding.ActivityMainBinding
import com.example.babycrydetectionapp.databinding.ContactsActivityBinding
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity

class TutorialFragment2 : Fragment() {
    private lateinit var addGuide: GuideView
    private lateinit var importGuide: GuideView
    private lateinit var searchGuide: GuideView
    private lateinit var binding: ContactsActivityBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = ContactsActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbarMenu()

        addGuide.show()
        addGuide.dismiss()

        importGuide.show()
        importGuide.dismiss()

        searchGuide.show()
        searchGuide.dismiss()
    }

    private fun setupToolbarMenu() {
        binding.contactsToolbar.inflateMenu(R.menu.contacts_menu)
        binding.contactsToolbar.menu.findItem(R.id.contacts_menu_search).actionView.isEnabled = false

        addGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.Add_button))
            .setContentText(getString(R.string.Used_for_adding_contacts))
            .setGravity(Gravity.auto) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView( requireView().findViewById(R.id.contacts_menu_add))
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .build()

        importGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.Import_button))
            .setContentText(getString(R.string.Used_for_importing))
            .setGravity(Gravity.auto) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView( requireView().findViewById(R.id.contacts_menu_import))
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .build()

        searchGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.Menu_Button))
            .setContentText(getString(R.string.Navigation_menu))
            .setGravity(Gravity.auto) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView( requireView().findViewById(R.id.contacts_menu_search))
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .build()

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
                R.id.contacts_menu_search ->{
                    searchGuide.show()
                    true
                }
                else -> {false}
            }
        }

    }
}