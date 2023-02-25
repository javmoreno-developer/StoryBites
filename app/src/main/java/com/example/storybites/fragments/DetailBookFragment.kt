package com.example.storybites.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.storybites.R
import com.example.storybites.databinding.FragmentDetailBookBinding
import com.example.storybites.objects.Book
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


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
        // sacamos el nombre del usuario a partir del uid del libro y comprobamos si el libro le pertenece
        db.collection("user").document(book.uid)
            .get()
            .addOnSuccessListener {


                if(book.uid == uidUser) {
                    pertenece = true;
                }
                overData(book,it.get("name").toString())
            }

        binding.btnDel.setOnClickListener {
            showDialog(book.docId)
        }
        binding.btnAct.setOnClickListener {
            findNavController().navigate(DetailBookFragmentDirections.actionDetailBookFragmentToCoreFragment(book))
        }
    }
    fun overData(book: Book, name: String) {
        with(binding) {
            print(book)
            descDetail.setText(book.desc)
            titleDetail.setText("${book.title} by ${name}")
            GoalDetail.setText(book.goal)
            DurDetail.setText(book.duration.toString())
            PunDetail.setText(book.puntuacion.toString())

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
    fun showDialog(uid: String) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("¿Quieres borrar el texto?")
            .setNegativeButton("No") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Si") { dialog, which ->
                // Respond to positive button press
                print(uid);
                db.collection("book").document(uid).delete()
                    .addOnSuccessListener {
                        print("borrado")
                    }
                    .addOnFailureListener {
                        print("fallo")
                    }
            }
            .show()
    }
}