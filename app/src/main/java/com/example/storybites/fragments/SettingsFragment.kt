package com.example.storybites.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storybites.BeginActivity
import com.example.storybites.GoalActivity
import com.example.storybites.R
import com.example.storybites.databinding.FragmentSettingsBinding
import com.example.storybites.objects.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.HashMap


class SettingsFragment : Fragment() {
    private val File = 1
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var db: FirebaseFirestore
    private var urlPhoto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                    Snackbar.make(binding.root,R.string.main_user_error,Snackbar.LENGTH_LONG).show()
                }
        }

        with(binding) {
            // borrar cuenta
            btnAccount.setOnClickListener {

                //Firebase.auth.signOut()

                val user = Firebase.auth.currentUser!!
                var uid = user.uid

                // borramos del servicio de autenticacion
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // borramos el registro de la coleccion
                            db.collection("user").document(uid).delete()
                                .addOnSuccessListener {
                                // redirigimos al login
                                activity?.finish()
                                return@addOnSuccessListener
                            }
                        }
                    }

            }

            btnActInfo.setOnClickListener {
                var email = userSettingsTextInput.text.toString()
                var name = nameSettingsTextInput.text.toString()
                var pass = passSettingsTextInput.text.toString()

                if(!pass.isNullOrEmpty() && !email.isNullOrEmpty() && !name.isNullOrEmpty()) {

                    var map = mutableMapOf<String, Any>()
                    map.set("email", email)
                    map.set("name", name)
                    map.set("picture", urlPhoto)

                    it.isEnabled = false
                    //map.set("pass",pass)
                    // actualizamos email y nombre de usuario
                    db.collection("user").document(uid).update(map)
                        .addOnSuccessListener {
                            // actualizamos la pass del user y el email (firebase auth)
                            currentUser?.updateEmail(email)
                            currentUser?.updatePassword(pass)
                            binding.btnActInfo.isEnabled = true

                            Snackbar.make(
                                binding.root,
                                getString(R.string.settings_snack),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                } else {
                    Snackbar.make(binding.root,getString(R.string.settings_dialog_err), Snackbar.LENGTH_LONG).show()
                }
            }

            binding.btnGoal.setOnClickListener {
                // Navegamos a la pantalla de goal
                val i = Intent(binding.root.context,GoalActivity::class.java)
                i.putExtra("act",true)
                startActivity(i)
            }

        }





    }



}