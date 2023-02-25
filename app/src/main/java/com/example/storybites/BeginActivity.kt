package com.example.storybites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BeginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_begin)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
}