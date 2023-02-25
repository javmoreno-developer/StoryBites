package com.example.storybites.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.storybites.R
import com.example.storybites.databinding.FragmentCoreBinding
import com.example.storybites.objects.Book
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class CoreFragment : Fragment() {

    private lateinit var binding: FragmentCoreBinding
    private lateinit var db: FirebaseFirestore
    private var actualizar: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoreBinding.inflate(inflater,container,false);

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val args = CoreFragmentArgs.fromBundle(requireArguments())
        val bookUpdate = args.book as Book;
        if(bookUpdate.docId == "") {
            actualizar = false
        } else {
            actualizar = true
            setData(bookUpdate);
        }
        print(args);



        with(binding) {
            doneBtn.setOnClickListener {
            if(actualizar) {
                showdialogAct(bookUpdate.docId)
            } else {
                showdialog()
            }

            }
        }
        // escondemos el navMenu

    }
    fun setData(book: Book) {
        with(binding) {
            descTextInput.setText(book.desc)
            durTextInput.setText(book.duration.toString())
            titleTextInput.setText(book.title)
            goalTextInput.setText(book.goal)
            contentTextInput.setText(book.content)

        }
    }
    fun showdialog() {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Guardar historia")
            .setNegativeButton("No") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Si") { dialog, which ->
                // Respond to positive button press
                with(binding) {
                    var desc = descTextInput.text.toString()
                    var dur = durTextInput.text.toString().toInt()
                    var title = titleTextInput.text.toString()
                    var goal = goalTextInput.text.toString()
                    var content = contentTextInput.text.toString()
                    //val uid = Firebase.auth.currentUser?.uid

                    var book: Book = Book("",title,content,desc,dur,0,goal,"xW2rrlOLSHSLSedKZpi9QXjbbw42","")

                    db = FirebaseFirestore.getInstance()

                    var newBook = db.collection("book").document()
                    book.docId = newBook.id;
                    newBook.set(book).addOnSuccessListener {
                        Log.i("xxx","finish bbook")
                    }
                        .addOnFailureListener {
                            Log.i("xxx","fail book")
                        }
                }
            }
            .show()
    }

    fun showdialogAct(docId: String) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Actualizar historia")
            .setNegativeButton("No") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Si") { dialog, which ->
                // Respond to positive button press
                with(binding) {
                    var desc = descTextInput.text.toString()
                    var dur = durTextInput.text.toString().toInt()
                    var title = titleTextInput.text.toString()
                    var goal = goalTextInput.text.toString()
                    var content = contentTextInput.text.toString()
                    //val uid = Firebase.auth.currentUser?.uid

                    var book: Book = Book(docId,title,content,desc,dur,0,goal,"xW2rrlOLSHSLSedKZpi9QXjbbw42","")

                    db = FirebaseFirestore.getInstance()

                    db.collection("book").document(docId).set(book)
                        .addOnSuccessListener {
                            print("actualizado")
                        }
                        .addOnFailureListener {
                            print("fail")
                        }

                }
            }
            .show()
    }



}