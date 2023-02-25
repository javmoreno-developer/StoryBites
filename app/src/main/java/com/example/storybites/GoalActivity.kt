package com.example.storybites

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.storybites.adapters.GoalAdapter
import com.example.storybites.databinding.ActivityGoalBinding
import com.example.storybites.objects.Goal
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class GoalActivity : AppCompatActivity() {
    private lateinit var binding:ActivityGoalBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var goalAdapter: GoalAdapter
    private var lista: MutableList<Goal> = mutableListOf()
    private var contadorDone: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //cargamos las tags
        loadTag()

        // done button click

        binding.btnGoalClose.setOnClickListener {
           if(contadorDone.size >=3) {
             val i = Intent(this@GoalActivity,MainActivity::class.java)
             startActivity(i)
           } else {
               Snackbar.make(binding.root,"Debes seleccionar al menos 3 categorias",Snackbar.LENGTH_LONG).show()
           }
        }


    }
    fun loadTag() {
        Log.i("Datos empieza","a")
        val goal = db.collection("goal").get()
            .addOnSuccessListener { resultado ->
                Log.i("Datos suc","Success")

                for(documento in resultado) {
                    Log.i("Datos: ","${documento.data}")
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
                    goalAdapter = GoalAdapter(lista)

                    // click listener
                    goalAdapter.setOnItemClickListener {
                        goalClick(it)
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

    fun goalClick(param: Int) {
        Snackbar.make(binding.root,"Has pulsado ${lista[param].gid}",Snackbar.LENGTH_LONG).show()
        if(contadorDone.indexOf(param) == -1) {
            contadorDone.add(param)
        } else {
            contadorDone.remove(param)
            binding.btnGoalClose.imageTintList = ColorStateList.valueOf(Color.parseColor("#7E7F9A"))
        }

        if(contadorDone.size >=3) {
            Snackbar.make(binding.root,"Done",Snackbar.LENGTH_LONG).show()

            binding.btnGoalClose.imageTintList = ColorStateList.valueOf(Color.parseColor("#F9F8F8"))
            //binding.btnGoalClose.backgroundTintMode = ColorStateList.valueOf(Color.parseColor("#F9F8F8"))

        // btnGoal.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
        }

        //goalAdapter.notifyItemChanged(param);

    }
}