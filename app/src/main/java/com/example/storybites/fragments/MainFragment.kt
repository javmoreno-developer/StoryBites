package com.example.storybites.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storybites.R
import com.example.storybites.adapters.CardMainAdapter
import com.example.storybites.databinding.FragmentMainBinding
import com.example.storybites.objects.Book
import com.example.storybites.objects.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: CardMainAdapter
    private lateinit var UserGoals: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        with(binding) {
            floatingActionButton.setOnClickListener {
                var book = Book("","","","",0,"","","")
                binding.root.findNavController().navigate(MainFragmentDirections.actionMainFragmentToCoreFragment(book))

            }

        }

        db = FirebaseFirestore.getInstance()
        // obtenemos el usuario logueado
        val currentUser = Firebase.auth.currentUser


        if (currentUser != null) {

            // obtenemos el documento del usuario logueado
            var user: User = User("","","","",null);

             db.collection("user").document(currentUser.uid).get()
                .addOnSuccessListener {
                    if(it!=null) {
                        var uid = it.get("uid") as String
                        var email = it.get("email") as String
                        var name = it.get("name") as String
                        var goals = it.get("goals") as MutableList<String>
                        user = User(uid, name, email, "", goals)

                        UserGoals = goals
                        binding.mainFragmentGreeting.text = resources.getString(R.string.main_fragment_greeting,name)
                        listBooks()
                    }
                }
                .addOnFailureListener {
                    Snackbar.make(binding.root,R.string.main_user_error,Snackbar.LENGTH_LONG).show()
                }
        }

        //instanciamos el recycler view

    }

    fun listBooks() {
        with(binding) {
            var lista = mutableListOf<Book>()

            db.collection("book")
                .get()
                .addOnSuccessListener {
                    for(item in it.documents) {
                        lista.add(item.toObject(Book::class.java) as Book)



                    }




                    adapter = CardMainAdapter(lista,UserGoals)
                    adapter.setOnItemClickListener {
                        var book = lista[it]
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailBookFragment(book))
                    }
                    mainFragmentRvFirst.adapter = adapter
                    mainFragmentRvFirst.layoutManager= GridLayoutManager(binding.root.context,2)
                }
                .addOnFailureListener {
                    Snackbar.make(binding.root,R.string.main_user_error,Snackbar.LENGTH_LONG).show()
                }


        }
    }


}