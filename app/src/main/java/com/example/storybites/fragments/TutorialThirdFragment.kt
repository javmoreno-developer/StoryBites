package com.example.storybites.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.storybites.BeginActivity
import com.example.storybites.GoalActivity
import com.example.storybites.R
import com.example.storybites.databinding.FragmentTutorialThirdBinding


class TutorialThirdFragment : Fragment() {

    private lateinit var binding: FragmentTutorialThirdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTutorialThirdBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.btnGetStarted.setOnClickListener {
            val i = Intent(it.context,BeginActivity::class.java)
            startActivity(i)
        }
    }

}