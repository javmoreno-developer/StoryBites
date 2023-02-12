package com.example.storybites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.storybites.databinding.ActivityTutorialBinding
import com.example.storybites.fragments.TutorialFirstFragment
import com.example.storybites.adapters.TutorialFragmentsAdapter
import com.example.storybites.fragments.TutorialSecondFragment
import com.example.storybites.fragments.TutorialThirdFragment
import me.relex.circleindicator.CircleIndicator3

class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tutorialAdapter: FragmentStateAdapter
    private lateinit var indicator: CircleIndicator3
    private var fragments: MutableList<Fragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragments.add(TutorialFirstFragment())
        fragments.add(TutorialSecondFragment())
        fragments.add(TutorialThirdFragment())

        viewPager = binding.viewPager
        tutorialAdapter = TutorialFragmentsAdapter(this, fragments)
        viewPager.adapter = tutorialAdapter
        indicator = binding.circleIndicator
        indicator.setViewPager(viewPager)
        handleOnBackPressed()
    }


    private fun handleOnBackPressed() {
        // New method to override the back pressed button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            /**
             *
             */
            override fun handleOnBackPressed() {
                if(viewPager.currentItem == 0) {
                    finish()
                } else {
                    viewPager.currentItem = viewPager.currentItem - 1
                }
            }
        })
    }
}
