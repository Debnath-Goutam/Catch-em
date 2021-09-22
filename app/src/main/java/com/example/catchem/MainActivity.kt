package com.example.catchem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.start).setOnClickListener{
            if(findViewById<EditText>(R.id.usr_name).text.isEmpty())
                Toast.makeText(this, "User Name cannot be empty", Toast.LENGTH_SHORT).show()
            else {
                val name = findViewById<EditText>(R.id.usr_name).text.toString()
                val intent = Intent(this, Display::class.java)
                intent.putExtra(Data.usr_name, name)
                startActivity(intent)
                finish()
            }

        }
    }
}