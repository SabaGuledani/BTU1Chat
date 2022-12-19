package com.saba.messagingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saba.messagingapp.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val email = binding.email
        val password = binding.password
        val btn = binding.createbtn

        var user:FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()

        if(user!=null){
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("true",true)
            startActivity(intent)
            finish()
        }

            btn.setOnClickListener {
                if (email.editText?.text.toString()
                        .isValidEmail() && password.editText?.text.toString() != ""
                    && password.editText?.text.toString().length >= 6
                ) {
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(
                            email.editText?.text.toString(),
                            password.editText?.text.toString()
                        )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("true", true)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }

                }
            }


    }

    fun String.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()


}