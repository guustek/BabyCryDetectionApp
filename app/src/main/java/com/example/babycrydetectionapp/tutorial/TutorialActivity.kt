package com.example.babycrydetectionapp.tutorial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.babycrydetectionapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

private const val NUM_PAGES = 2

class TutorialActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        viewPager = findViewById(R.id.viewPager)
        navigation = findViewById(R.id.bottomNavigationView)
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        navigation.menu.findItem(R.id.tutorial_navigate_next).isChecked = false
        navigation.menu.findItem(R.id.tutorial_navigate_previous).isChecked = false
        navigation.menu.findItem(R.id.tutorial_navigate_next).isEnabled = true
        navigation.menu.findItem(R.id.tutorial_navigate_previous).isEnabled = false
        navigation.menu.findItem(R.id.tutorial_navigate_next).isCheckable = false
        navigation.menu.findItem(R.id.tutorial_navigate_previous).isCheckable = false
        (navigation as NavigationBarView).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tutorial_navigate_next -> {
                    viewPager.currentItem = viewPager.currentItem + 1
                    it.isEnabled = viewPager.currentItem != NUM_PAGES - 1
                    navigation.menu.findItem(R.id.tutorial_navigate_previous).isEnabled = viewPager.currentItem != 0
                    true
                }
                R.id.tutorial_navigate_previous -> {
                    viewPager.currentItem = viewPager.currentItem - 1
                    it.isEnabled = viewPager.currentItem != 0
                    navigation.menu.findItem(R.id.tutorial_navigate_next).isEnabled =
                        viewPager.currentItem != NUM_PAGES - 1
                    true
                }
                else -> false
            }
        }
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                navigation.menu.findItem(R.id.tutorial_navigate_next).isEnabled = true
                navigation.menu.findItem(R.id.tutorial_navigate_previous).isEnabled = true
                if (position == NUM_PAGES-1)
                    navigation.menu.findItem(R.id.tutorial_navigate_next).isEnabled = false
                if(position == 0)
                    navigation.menu.findItem(R.id.tutorial_navigate_previous).isEnabled = false
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            if (position == 0) return TutorialFragment1()
            if (position == 1) return TutorialFragment2()
            return TutorialFragment1()
        }

    }
}