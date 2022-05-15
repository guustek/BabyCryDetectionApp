package com.example.babycrydetectionapp.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.babycrydetectionapp.R
import com.example.babycrydetectionapp.SettingsActivity
import com.example.babycrydetectionapp.contacts.ContactsActivity
import com.example.babycrydetectionapp.databinding.ActivityMainBinding
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType


class TutorialFragment1 : Fragment() {
    private lateinit var listenGuide: GuideView
    private lateinit var timerGuide: GuideView
    private lateinit var menuGuide: GuideView
    private lateinit var guideGuide: GuideView
    private lateinit var contactsGuide: GuideView
    private lateinit var settingsGuide: GuideView
    private lateinit var binding: ActivityMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
        menuGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.Menu_Button))
            .setContentText(getString(R.string.Navigation_menu))
            .setGravity(Gravity.center) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView( binding.button)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .setPointerType(PointerType.circle)
            .build()

        timerGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.Timer))
            .setContentText(getString(R.string.timer_message))
            .setGravity(Gravity.center) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView( binding.timer)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .setPointerType(PointerType.circle)
            .build()

        listenGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.Start_Button))
            .setContentText(getString(R.string.Starts_listening_for_baby_cry))
            .setGravity(Gravity.center) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView( binding.classifyButton)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .setPointerType(PointerType.circle)
            .build()

////        guideGuide = GuideView.Builder(context)
////            .setTitle(getString(R.string.Start_Button))
////            .setContentText(getString(R.string.Starts_listening_for_baby_cry))
////            .setGravity(Gravity.auto) //optional
////            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
////            .setTargetView( requireView().findViewById(R.id.menu_tutorial))
////            .setContentTextSize(12) //optional
////            .setTitleTextSize(14) //optional
////            .build()
//
//
//
////        settingsGuide = GuideView.Builder(context)
////            .setTitle(getString(R.string.Start_Button))
////            .setContentText(getString(R.string.Starts_listening_for_baby_cry))
////            .setGravity(Gravity.auto) //optional
////            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
////            .setTargetView( requireView().findViewById(R.id.menu_settings))
////            .setContentTextSize(12) //optional
////            .setTitleTextSize(14) //optional
////            .build()
//
//        binding.navView.setNavigationItemSelectedListener {
//            when (it.itemId) {
////                R.id.menu_tutorial -> guideGuide.show()
//                R.id.menu_contacts -> contactsGuide.show()
////                R.id.menu_settings -> settingsGuide.show()
//            }
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//            false
//        }
//        binding.navView.bringToFront()

        listenGuide.show()
        listenGuide.dismiss()

        timerGuide.show()
        timerGuide.dismiss()

        menuGuide.show()
        menuGuide.dismiss()

        binding.classifyButton.setOnClickListener{
            listenGuide.show()
        }

        binding.timer.setOnClickListener{
            timerGuide.show()
        }

        binding.button.setOnClickListener{
            menuGuide.show()
        }

    }
}