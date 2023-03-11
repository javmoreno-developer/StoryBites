package com.example.storybites.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.storybites.GoalActivity
import com.example.storybites.MainActivity
import com.example.storybites.R
import com.example.storybites.databinding.FragmentSignInBinding
import com.example.storybites.objects.User
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private var newby: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val args = SignInFragmentArgs.fromBundle(requireArguments())




        // Cambiamos los input
        if(args.user != null) {
            with(binding) {
                userLoginTextInput.setText(args.user!!.email)
                passLoginTextInput.setText(args.user!!.pass)
                newby = true
            }
        }
        // Navegar al sign up fragment
        binding.btnToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        with(binding) {


            // hacer sign in
            btnGetStarted.setOnClickListener {
                var email = userLoginTextInput.text.toString()
                var pass = passLoginTextInput.text.toString()

                it.isEnabled = false
                Firebase.auth
                    .signInWithEmailAndPassword(email,pass)
                    .addOnSuccessListener {
                       // volvemos al fragmento original
                        //findNavController().navigate(R.id.action_signInFragment_to_introFragment)

                        if (!newby) {
                            val i = Intent(binding.root.context, MainActivity::class.java);
                            startActivity(i)
                        } else {
                            val i = Intent(binding.root.context, GoalActivity::class.java);
                            i.putExtra("act",false)
                            startActivity(i)
                            binding.btnGetStarted.isEnabled = true
                        }
                    }
                    .addOnFailureListener {
                        Snackbar.make(binding.root,getString(R.string.sigin_err), Snackbar.LENGTH_LONG).show()
                        binding.btnGetStarted.isEnabled = true
                    }

            }
        }
    }





}