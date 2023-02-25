package com.example.storybites.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storybites.databinding.CeldaMainFirstBinding
import com.example.storybites.objects.Book
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CardMainAdapter(val lista: MutableList<Book>): RecyclerView.Adapter<CardMainAdapter.BookViewHolder>(){



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

                cardTitle.setText(book.title)
                cardContainer.setOnClickListener {onItemClickListener(adapterPosition) }
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