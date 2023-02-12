package com.example.storybites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.storybites.databinding.ActivitySignUpBinding
import com.example.storybites.objects.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding;
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            // volver a sign in
            btnOutSignUp.setOnClickListener {
                setResult(RESULT_CANCELED);
                finish();
                return@setOnClickListener
            }

            // registrarse
            btnGetRegister.setOnClickListener {

                var email = userLoginTextInput.text.toString()
                var pass = passLoginTextInput.text.toString()
                var name = nameLoginTextInput.text.toString()
                Log.i("XXX","registrar pulsado")

                var user = User(name,email, pass)

                Firebase.auth.createUserWithEmailAndPassword(email,pass)
                   .addOnSuccessListener {

                   }
                    .addOnFailureListener {
                        Log.i("AAA","Failure")
                    }
                db.collection("users").add(user)
                    .addOnSuccessListener {
                        val intencion = Intent()


                        intencion.putExtra("_usuario",user)
                        setResult(RESULT_OK,intencion)
                        finish();
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener {
                        Log.i("AAA","Failure2")
                    }
            }

        }
    }

}