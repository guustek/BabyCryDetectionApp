package com.example.babycrydetectionapp.tutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.babycrydetectionapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

private const val NUM_PAGES = 5

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
        navigation.menu.findItem(R.id.tutorial_navigate_previous).isEnabled= false
        navigation.menu.findItem(R.id.tutorial_navigate_next).isCheckable = false
        navigation.menu.findItem(R.id.tutorial_navigate_previous).isCheckable = false
        (navigation as NavigationBarView).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tutorial_navigate_next -> {
                    viewPager.currentItem = viewPager.currentItem + 1
                    it.isEnabled = viewPager.currentItem != NUM_PAGES-1
                    navigation.menu.findItem(R.id.tutorial_navigate_previous).isEnabled= viewPager.currentItem != 0
                    true
                }
                R.id.tutorial_navigate_previous -> {
                    viewPager.currentItem = viewPager.currentItem - 1
                    it.isEnabled = viewPager.currentItem != 0
                    navigation.menu.findItem(R.id.tutorial_navigate_next).isEnabled = viewPager.currentItem != NUM_PAGES-1
                    true
                }
                else -> false
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            if (position == 0) return TutorialFragment1()
            if (position == 1) return TutorialFragment2()
            if (position == 2) return TutorialFragment3()
            if (position == 3) return TutorialFragment4()
            if (position == 4) return TutorialFragment5()
            return TutorialFragment1()
        }
    }
}