package com.example.storybites.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storybites.R
import com.example.storybites.databinding.FragmentSettingsBinding
import com.example.storybites.objects.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        db = FirebaseFirestore.getInstance()
        var uid = "";
        // obtenemos el usuario logueado
        val currentUser = Firebase.auth.currentUser
        // obtener el documento del usuario
        if (currentUser != null) {


            // obtenemos el documento del usuario logueado
            var user: User = User("","","","",null);

            db.collection("user").document(currentUser.uid).get()
                .addOnSuccessListener {
                    if(it!=null) {
                        uid = it.get("uid") as String;
                        var email = it.get("email") as String
                        var name = it.get("name") as String


                        with(binding) {
                            nameSettingsTextInput.setText(name)
                            userSettingsTextInput.setText(email)

                        }




                    }
                }
                .addOnFailureListener {
                    Log.i("XXX","Error en la lectura del documento del usuario logueado")
                }
        }

        with(binding) {
            btnActInfo.setOnClickListener {
                var email = userSettingsTextInput.text.toString()
                var name = nameSettingsTextInput.text.toString()
                var pass = passSettingsTextInput.text.toString()

                Log.i("XXX",email)
                Log.i("XXX",name)
                Log.i("XXX",pass)

                var map = mutableMapOf<String,Any>()
                map.set("email",email)
                map.set("name",name)
                //map.set("pass",pass)
                // actualizamos email y nombre de usuario
                db.collection("user").document(uid).update(map)
                    .addOnSuccessListener {
                        // actualizamos la pass del user y el email (firebase auth)
                        currentUser?.updateEmail(email)
                        currentUser?.updatePassword(pass)
                    }
            }
        }

        //instanciamos el recycler view



    }


}