package com.example.storybites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.storybites.databinding.ActivityLoginBinding
import com.example.storybites.objects.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val registerForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        registerOp(it)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            // registrarse
            btnToSignUp.setOnClickListener {
                val i = Intent(this@LoginActivity,SignUpActivity::class.java)
                registerForResult.launch(i)

            }

            // hacer sign in
            btnGetStarted.setOnClickListener {
                var email = userLoginTextInput.text.toString()
                var pass = passLoginTextInput.text.toString()
                Firebase.auth
                    .signInWithEmailAndPassword(email,pass)
                    .addOnSuccessListener {
                        val i = Intent(this@LoginActivity,IntroActivity::class.java);
                        startActivity(i);
                }
            }
        }
    }
    private fun registerOp(respuesta : androidx.activity.result.ActivityResult) {
        val mensaje = when(respuesta.resultCode) {
            RESULT_OK -> "Se ha registrado el usuario"
            RESULT_CANCELED -> "No se ha registrado el usuario"
            else -> ""
        }



        if(respuesta.resultCode == RESULT_OK) {
            val usuario = respuesta.data?.extras?.get("_usuario") as User

            with(binding) {
                userLoginTextInput.setText(usuario.email.toString())
                passLoginTextInput.setText(usuario.pass.toString())
            }
        }

    }
}