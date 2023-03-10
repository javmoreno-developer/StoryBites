package com.example.storybites.objects

class User(var uid: String = "",
           var name : String? = "",
           var email: String = "",
           var pass: String = "",
           var goals: MutableList<String>? = mutableListOf()
           ) : java.io.Serializable

