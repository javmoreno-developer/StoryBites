package com.example.storybites.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storybites.R
import com.example.storybites.databinding.FragmentReadBinding


class ReadFragment : Fragment() {

private lateinit var binding: FragmentReadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReadBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val args = ReadFragmentArgs.fromBundle(requireArguments())
        val book = args.book

        // sobrescribimos los datos
        with(binding) {
            editCore.setText(book?.content ?: "")
        }
    }

}