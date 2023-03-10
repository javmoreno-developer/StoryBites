package com.example.storybites.fragments

import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.storybites.R
import com.example.storybites.databinding.FragmentCoreBinding
import com.example.storybites.objects.Book
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_core.*
import java.util.HashMap


class CoreFragment : Fragment() {
    private val File = 1

    private lateinit var binding: FragmentCoreBinding
    private lateinit var db: FirebaseFirestore
    private var actualizar: Boolean = false
    private var urlPhoto: String = ""
    private var goalIdList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCoreBinding.inflate(inflater,container,false)


        // obtenemos los goals
        val spinnerItems = mutableListOf<String>()
        db.collection("goal").get().addOnSuccessListener {
            for(document in it.documents) {
                spinnerItems.add(document.get("title") as String)
                goalIdList.add(document.get("gid") as String)
            }
            // rellenamos el spinner
            val adapterSpinner = ArrayAdapter(binding.root.context,android.R.layout.simple_list_item_1,spinnerItems)
            binding.goalSpinner.adapter = adapterSpinner
        }

        return binding.root

    }

    override fun onResume() {
        super.onResume()

        val args = CoreFragmentArgs.fromBundle(requireArguments())
        val bookUpdate = args.book as Book





        if(bookUpdate.docId == "") {
            actualizar = false
        } else {
            actualizar = true
            setData(bookUpdate)
        }

        // arrow back
        binding.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_coreFragment_to_mainFragment)
        }



        with(binding) {
            // imagen
            imageCore.setOnClickListener {
                fileUpload()
            }
            doneBtn.setOnClickListener {
                //comprobamos si estan todos los campos rellenos
                val desc = descTextInput.text.toString()
                val dur = durTextInput.text.toString()
                val title = titleTextInput.text.toString()
                val goal = goalIdList[binding.goalSpinner.selectedItemId.toInt()]
                val content = content_TextInput.text.toString()


                if(urlPhoto.isEmpty() || desc.isEmpty() || dur.isEmpty() || title.isEmpty() || goal.isEmpty() || content.isEmpty()) {
                    showDialogFail()
                } else {
                    if (actualizar) {
                        showdialogAct(bookUpdate.docId,desc,dur.toLong(),title,goal,content)
                    } else {
                        showdialog(desc,dur.toLong(),title,goal,content)
                    }
                }


            }
        }
        // escondemos el navMenu

    }

    private fun setData(book: Book) {
        with(binding) {
            descTextInput.setText(book.desc)
            durTextInput.setText(book.duration.toString())
            titleTextInput.setText(book.title)
            //goalTextInput.setText(book.goalId)
            content_TextInput.setText(book.content)
            Glide.with(binding.root).load(book.photo).into(binding.imageCore)
            urlPhoto = book.photo
        }
    }
    private fun showdialog(desc: String, dur: Long, title: String, goal: String, content: String) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Guardar historia")
            .setNegativeButton("No") { _, _ ->
                // Respond to negative button press
            }
            .setPositiveButton("Si") { _, _ ->
                // Respond to positive button press
                with(binding) {

                    val currentUser = Firebase.auth.currentUser

                    val book = Book("",title,content,desc,dur,goal, currentUser!!.uid,urlPhoto)



                    val newBook = db.collection("book").document()
                    book.docId = newBook.id
                    newBook.set(book).addOnSuccessListener {
                        findNavController().navigate(R.id.action_coreFragment_to_mainFragment)
                    }
                }
            }
            .show()
    }

    fun showdialogAct(docId: String,desc: String,dur: Long,title: String,goal: String,content: String) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Actualizar historia")
            .setNegativeButton("No") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Si") { dialog, which ->
                // Respond to positive button press
                with(binding) {

                    val currentUser = Firebase.auth.currentUser

                    val book = Book(docId,title,content,desc,dur,goal,currentUser!!.uid,urlPhoto)

                    db = FirebaseFirestore.getInstance()

                    db.collection("book").document(docId).set(book)
                        .addOnSuccessListener {
                            print("actualizado")
                            findNavController().navigate(R.id.action_coreFragment_to_mainFragment)
                        }
                        .addOnFailureListener {
                            print("fail")
                        }

                }
            }
            .show()
    }

    fun fileUpload() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, File)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("Book")
                val file_name: StorageReference = Folder.child("file" + FileUri!!.lastPathSegment)




                file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                    file_name.getDownloadUrl().addOnSuccessListener { uri ->
                        val hashMap =
                            HashMap<String, String>()
                        hashMap["link"] = java.lang.String.valueOf(uri)
                        // sobreescribmos la imagen
                        Glide.with(binding.root).load(hashMap["link"]).into(binding.imageCore)
                        urlPhoto = hashMap["link"].toString();
                        // ponemos
                    }
                }
            }
        }
    }
    fun showDialogFail() {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Faltan datos por rellenar")
            .setPositiveButton("Aceptar") { dialog, which -> }
            .show()
    }
}