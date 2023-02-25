package com.example.storybites.objects

data class Book(var docId: String = "",
                var title: String ="",
                var content: String ="",
                var desc: String ="",
                var duration: Int=0,
                var puntuacion: Int = 0,
                var goal: String ="",
                var uid: String="",
                var photo: String="") : java.io.Serializable