package com.example.storybites.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.storybites.R
import com.example.storybites.databinding.FragmentSignInBinding
import com.example.storybites.databinding.FragmentSignUpBinding
import com.example.storybites.objects.Goal
import com.example.storybites.objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding;
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()


        // Navegar al sign in fragment
        binding.btnOutSignUp.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(null))

        }

        with(binding) {

            // registrarse
            btnGetRegister.setOnClickListener {

                var email = userLoginTextInput.text.toString()
                var pass = passLoginTextInput.text.toString()
                var name = nameLoginTextInput.text.toString()
                Log.i("XXX","registrar pulsado")

                var user: User = User("",name,email, pass,null)

                Firebase.auth.createUserWithEmailAndPassword(email,pass)
                    .addOnSuccessListener {
                        val userCreated = FirebaseAuth.getInstance().currentUser
                        val uid = userCreated?.uid
                        user.uid = uid!!

                        userCreated?.getIdToken(true)
                            ?.addOnSuccessListener {

                                val idToken = it.token

                                if (uid != null && idToken != null) {
                                    db.collection("user").document(uid).set(
                                        hashUser(name, email, uid, idToken, mutableListOf())
                                    )
                                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(user))
                                }
                            }
                    }
                    .addOnFailureListener {
                        Log.i("AAA","Failure")
                    }
                Log.i("XXX2",user.uid)

            }

        }

    }

    private fun hashUser(name: String, email: String, uid: String, idToken: String,goals: MutableList<Goal>): Map<String, Any> {
        return hashMapOf(
            "name" to name,
            "email" to email,
            "picture" to "",
            "uid" to uid,
            "token" to idToken,
            "goals" to goals,
            "provider" to "firebase",
        )
    }



}