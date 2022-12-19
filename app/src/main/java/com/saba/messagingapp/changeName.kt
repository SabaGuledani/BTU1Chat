package com.saba.messagingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class changeName : AppCompatActivity() {
    private lateinit var nameet:EditText
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_name)
        nameet = findViewById(R.id.name_change_et)
        button = findViewById(R.id.changebtn)
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        button.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("user")
                .child("$senderUid")
                .child("name")
                .setValue(nameet.text.toString())
        }



    }
}