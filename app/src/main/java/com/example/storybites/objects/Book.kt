package com.example.storybites.objects

data class Book(var docId: String = "",
                var title: String ="",
                var content: String ="",
                var desc: String ="",
                var duration: Long=0,
                var goalId: String ="",
                var uid: String="",
                var photo: String="",

                ) : java.io.Serializable