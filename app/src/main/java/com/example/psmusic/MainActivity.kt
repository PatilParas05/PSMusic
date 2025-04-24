package com.example.psmusic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if the activity has been shown before
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isEnterNameShown = sharedPreferences.getBoolean("EnterNameShown", false)

        if (isEnterNameShown) {
            // Skip the "Enter Name" activity and go directly to the next activity
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        } else {
            // Show the "Enter Name" activity
            setContentView(R.layout.activity_main)

            val editor = sharedPreferences.edit()
            val okButton = findViewById<Button>(R.id.okButton)
            val nameEditText = findViewById<EditText>(R.id.nameEditText)

            okButton.setOnClickListener {
                val name = nameEditText.text.toString()

                // Save the name and the flag to indicate the activity has been shown
                editor.putBoolean("EnterNameShown", true) // Flag for activity display
                editor.putString("UserName", name)       // Save user's name
                editor.apply()

                // Go to next activity
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}