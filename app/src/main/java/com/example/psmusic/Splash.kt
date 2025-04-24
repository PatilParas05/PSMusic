package com.example.psmusic

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.psmusic.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

            val intent = Intent(this@Splash, MainActivity::class.java)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 3000) // Delay in milliseconds

        }
    }
