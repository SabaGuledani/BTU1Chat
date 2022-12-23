package com.saba.messagingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class changeName : AppCompatActivity() {
    private lateinit var nameet:EditText
    private lateinit var button: Button
    private lateinit var buttonpass:Button
    private lateinit var passet:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_name)
        nameet = findViewById(R.id.name_change_et)
        button = findViewById(R.id.changebtn)
        passet = findViewById(R.id.password_change_et)
        buttonpass = findViewById(R.id.changepassbtn)
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        button.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("user")
                .child("$senderUid")
                .child("name")
                .setValue(nameet.text.toString())
        }
        buttonpass.setOnClickListener {

            val newPassword = passet.text.toString()

            if (newPassword.length > 6) {

                FirebaseAuth.getInstance()
                    .currentUser?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "პაროლი შეცვლილია", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                        }
                    }

            }

        }




    }
}