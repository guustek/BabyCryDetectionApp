package com.example.babycrydetectionapp.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.babycrydetectionapp.R
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
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.menu_button))
            .setContentText(getString(R.string.menu_button_description))
            .setGravity(Gravity.center) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView(binding.button)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .setPointerType(PointerType.circle)
            .build()

        timerGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.timer_description))
            .setContentText(getString(R.string.timer_message))
            .setGravity(Gravity.center) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView(binding.timer)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .setPointerType(PointerType.circle)
            .build()

        listenGuide = GuideView.Builder(context)
            .setTitle(getString(R.string.start_button))
            .setContentText(getString(R.string.listen_button_description))
            .setGravity(Gravity.center) //optional
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView(binding.classifyButton)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .setPointerType(PointerType.circle)
            .build()

        binding.navView.bringToFront()

        listenGuide.show()
        listenGuide.dismiss()

        timerGuide.show()
        timerGuide.dismiss()

        menuGuide.show()
        menuGuide.dismiss()

        binding.classifyButton.setOnClickListener {
            listenGuide.show()
        }

        binding.timer.setOnClickListener {
            timerGuide.show()
        }

        binding.button.setOnClickListener {
            menuGuide.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}