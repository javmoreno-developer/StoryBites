package com.example.storybites.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.storybites.databinding.GoalBinding
import com.example.storybites.objects.Goal
import kotlin.random.Random

class GoalAdapter(val lista: MutableList<Goal>,val userGoal: MutableList<String>): Adapter<GoalAdapter.GoalViewHolder>(){

    private var colorList: MutableList<String> = mutableListOf("#F3DE8A","#EB9486","#7E7F9A","#1CBC60")


    //click listener
    private var onItemClickListener: (String,Boolean) -> Unit = { s: String, b: Boolean -> }

    fun setOnItemClickListener(listener: (String,Boolean)->Unit) {
        this.onItemClickListener = listener
    }

    private var added: Boolean = false;

    // contenedor
    inner class GoalViewHolder(val binding: GoalBinding) : ViewHolder(binding.root) {
        fun bindItem(goal: Goal) {
            with(binding) {

                // id
                btnGoal.id = adapterPosition
                // asignamos el texto al boton
                btnGoal.setText(goal.title)
                // asignamos el color al boton
                var color = colorList[Random.nextInt(0,colorList.size)]

                btnGoal.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))

                if(userGoal.contains(goal.gid)) {
                    btnGoal.backgroundTintList = null
                    Log.i("xxx","marcado bitch")
                } else {
                    added = false
                }

                // asignamos el listener el boton
                btnGoal.setOnClickListener {
                    if(userGoal.contains(goal.gid)) {
                        added = true
                        userGoal.remove(goal.gid)
                        Log.i("xxx","marcado bitch")
                    } else {
                        added = false
                    }
                    onItemClickListener(lista[adapterPosition].gid,added)
                    if(btnGoal.backgroundTintList == null) {
                        btnGoal.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
                    } else {
                        btnGoal.backgroundTintList = null
                    }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GoalBinding.inflate(layoutInflater,parent,false)
        return GoalViewHolder(binding);
    }

    override fun getItemCount(): Int {
        return this.lista.size
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        return holder.bindItem(this.lista[position]);
    }
}