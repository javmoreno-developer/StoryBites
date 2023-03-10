package com.example.storybites

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.storybites.adapters.GoalAdapter
import com.example.storybites.databinding.ActivityGoalBinding
import com.example.storybites.objects.Goal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class GoalActivity : AppCompatActivity() {
    private lateinit var binding:ActivityGoalBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var goalAdapter: GoalAdapter
    private var lista: MutableList<Goal> = mutableListOf()
    private var goals : MutableList<String> = mutableListOf()
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        // cargamos los parametros que nos llegan
        val act: Boolean = intent.extras?.getBoolean("act") as Boolean

        if(!act) {
            binding.arrowBack.visibility = View.GONE
        }

        binding.arrowBack.setOnClickListener {
            val i = Intent(this@GoalActivity,MainActivity::class.java)
            startActivity(i)

        }

        // cargamos los goals del usuario
        loadGoals()


        // done button click

        binding.btnGoalClose.setOnClickListener {
           if(goals.size == 3) {
               showDialog()
           } else {
               Snackbar.make(binding.root,"Debes seleccionar 3 categorias",Snackbar.LENGTH_LONG).show()
           }
        }


    }
    fun loadTag() {
        val goal = db.collection("goal").get()
            .addOnSuccessListener { resultado ->

                for(documento in resultado) {
                   // lista.add(element = documento as Goal)
                    var goal: Goal = Goal("","","");
                    goal.apply {
                        title = documento.data.get("title").toString()
                        desc = documento.data.get("desc").toString()
                        gid = documento.data.get("gid").toString()
                    }
                    lista.add(goal);
                }




                // cargamos el recycler view
                binding.rvGoal.apply {



                    goalAdapter = GoalAdapter(lista,goals)

                    // click listener
                    goalAdapter.setOnItemClickListener {gid,added ->
                        goalClick(gid,added)

                    }

                    adapter = goalAdapter
                    setHasFixedSize(true);
                    layoutManager = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
                }


            }
            .addOnFailureListener {
                Log.i("Datos fallo",it.toString())
            }


    }

    fun goalClick(gid: String,added: Boolean) {

        // vemos si añadimos la etiqueta
        if(added) {
            print("remove")
            Log.i("xxx65",goals.size.toString())
            goals.remove(gid)
        } else {
            print("added")
            Log.i("xxx65",goals.size.toString())
            goals.add(gid)
        }
        if(goals.size == 3) {
            binding.btnGoalClose.imageTintList = ColorStateList.valueOf(Color.parseColor("#F9F8F8"))
        } else {
            binding.btnGoalClose.imageTintList = ColorStateList.valueOf(Color.parseColor("#7E7F9A"))
        }
    }


    fun showDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Quieres asignar estos como tus gustos?")
            .setMessage("Estos influirán a la hora de ver algunas historias")
            .setNegativeButton("No") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Si") { dialog, which ->
                // asignamos los gustos al usuario


                db.collection("user").document(uid).update("goals",goals)
                    .addOnSuccessListener {
                        val i = Intent(this@GoalActivity,MainActivity::class.java)
                        startActivity(i)
                    }
                    .addOnFailureListener {
                        print("Fallo")
                    }
            }
            .show()
    }

    fun loadGoals() {
        uid = Firebase.auth.currentUser!!.uid
        db.collection("user").document(uid).get()
            .addOnSuccessListener {
                goals = it.get("goals") as MutableList<String>
                //cargamos las tags
                if(goals.size == 3) {
                    binding.btnGoalClose.imageTintList = ColorStateList.valueOf(Color.parseColor("#F9F8F8"))
                }
                loadTag()
            }
    }
}