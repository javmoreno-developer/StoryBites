package com.example.storybites.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.storybites.MainActivity
import com.example.storybites.R
import com.example.storybites.databinding.FragmentSignInBinding
import com.example.storybites.objects.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

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


        Log.i("XXX",args.user.toString());

        // Cambiamos los input
        if(args.user != null) {
            with(binding) {
                userLoginTextInput.setText(args.user!!.email)
                passLoginTextInput.setText(args.user!!.pass)
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
                Firebase.auth
                    .signInWithEmailAndPassword(email,pass)
                    .addOnSuccessListener {
                       // volvemos al fragmento original
                        //findNavController().navigate(R.id.action_signInFragment_to_introFragment)
                        val i = Intent(binding.root.context,MainActivity::class.java);
                        startActivity(i)
                    }
                    .addOnFailureListener {
                        Log.i("XXXv1",it.toString())
                    }
            }
        }
    }
    private fun registerOp(respuesta : androidx.activity.result.ActivityResult) {
        val mensaje = when(respuesta.resultCode) {
            AppCompatActivity.RESULT_OK -> "Se ha registrado el usuario"
            AppCompatActivity.RESULT_CANCELED -> "No se ha registrado el usuario"
            else -> ""
        }



        if(respuesta.resultCode == AppCompatActivity.RESULT_OK) {
            val usuario = respuesta.data?.extras?.get("_usuario") as User

            with(binding) {
                userLoginTextInput.setText(usuario.email.toString())
                passLoginTextInput.setText(usuario.pass.toString())
            }
        }


    }

}