package com.example.storybites.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.storybites.R
import com.example.storybites.databinding.FragmentDetailBookBinding
import com.example.storybites.objects.Book
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class DetailBookFragment : Fragment() {

    private lateinit var binding: FragmentDetailBookBinding
    private val db = FirebaseFirestore.getInstance()
    private var pertenece: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBookBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val args = DetailBookFragmentArgs.fromBundle(requireArguments())
        val book = args.book as Book
        var uidUser = Firebase.auth.currentUser?.uid

        // arrow back
        binding.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailBookFragment_to_mainFragment)
        }

        // sacamos el nombre del usuario a partir del uid del libro y comprobamos si el libro le pertenece
        db.collection("user").document(book.uid)
            .get()
            .addOnSuccessListener {

                var name = it.get("name") as String
                if(book.uid == uidUser) {
                    pertenece = true;
                }
                // obtenemos el nombre del goal a partir del gid
                db.collection("goal").document(book.goalId).get().addOnSuccessListener {


                    overData(book,name,it.get("title") as String)
                }

            }

        binding.btnDel.setOnClickListener {
            showDialog(book)
        }
        binding.btnAct.setOnClickListener {
            findNavController().navigate(DetailBookFragmentDirections.actionDetailBookFragmentToCoreFragment(book))
        }
    }
    fun overData(book: Book, name: String, goalTitle: String) {
        with(binding) {
            print(book)
            descDetail.setText(book.desc)
            titleDetail.setText("${book.title} by ${name}")
            GoalDetail.setText(goalTitle)
            DurDetail.setText(book.duration.toString() + "min")


            // imagen
            if(book.photo != "") {
                Glide.with(binding.root).load(book.photo).into(binding.detailImg)
            }

            btnRead.setOnClickListener {
                findNavController().navigate(DetailBookFragmentDirections.actionDetailBookFragmentToReadFragment(book))
            }

            if(pertenece) {
                // ocultamos los botones de leer y actualizar

                    btnDel.visibility = View.VISIBLE
                    btnAct.visibility = View.VISIBLE

            }

        }
    }
    fun showDialog(book: Book) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("¿Quieres borrar la historia?")
            .setNegativeButton("No") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Si") { dialog, which ->
                // Respond to positive button press

                db.collection("book").document(book.docId).delete()
                    .addOnSuccessListener {

                        findNavController().navigate(R.id.action_detailBookFragment_to_mainFragment)
                    }
                    .addOnFailureListener {
                       Snackbar.make(binding.root,"Error al borrar la historia",Snackbar.LENGTH_LONG).show()
                    }


            }
            .show()
    }
}