package com.example.storybites.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.storybites.MainActivity
import com.example.storybites.R
import com.example.storybites.TutorialActivity
import com.example.storybites.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {
    private lateinit var binding: FragmentIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // vamos al fragmento sign in
        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToSignInFragment(null))
            /*val i = Intent(it.rootView.context,MainActivity::class.java);
            startActivity(i)*/
        }

        // vamos al tutorial
        binding.btnGetTutorial.setOnClickListener {
            val i = Intent(it.rootView.context,TutorialActivity::class.java);
            startActivity(i)
        }
    }
}