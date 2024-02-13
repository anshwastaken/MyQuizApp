package com.example.myquizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myquizapp.databinding.ActivityForgotBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    lateinit var forgotbinding : ActivityForgotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        forgotbinding = ActivityForgotBinding.inflate(layoutInflater)
        val view = forgotbinding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        forgotbinding.ForgotButton.setOnClickListener {
            val email = forgotbinding.forgotemailedit.text.toString()
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext,"send Link to email",Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}