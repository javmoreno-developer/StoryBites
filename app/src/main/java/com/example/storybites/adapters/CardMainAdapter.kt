package com.example.storybites.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storybites.databinding.CeldaMainFirstBinding
import com.example.storybites.objects.Book
import com.example.storybites.objects.Goal
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CardMainAdapter(val lista: MutableList<Book>,val goals: MutableList<String>): RecyclerView.Adapter<CardMainAdapter.BookViewHolder>(){



    //click listener
    private var onItemClickListener: (Int) -> Unit = {}

    fun setOnItemClickListener(listener: (Int)->Unit) {
        this.onItemClickListener = listener
    }

    private var uid = Firebase.auth.currentUser?.uid;

    // contenedor
    inner class BookViewHolder(val binding: CeldaMainFirstBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(book: Book) {
            with(binding) {

                if(book.uid != uid!!) imgHolder.visibility = View.GONE

                if(!goals.contains(book.goalId)) imgGoalHolder.visibility = View.GONE


                cardTitle.setText(book.title)
                cardContainer.setOnClickListener {onItemClickListener(adapterPosition) }
                //ponemos imagen
                if(book.photo != "") {
                    Glide.with(binding.root).load(book.photo).into(binding.backCard)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CeldaMainFirstBinding.inflate(layoutInflater,parent,false)
        return BookViewHolder(binding);
    }

    override fun getItemCount(): Int {
        return this.lista.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        return holder.bindItem(this.lista[position]);
    }
}