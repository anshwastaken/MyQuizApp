package com.example.myquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myquizapp.databinding.ActivityNewUserBinding
import com.google.firebase.auth.FirebaseAuth

class NewUserActivity : AppCompatActivity() {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var newUserBinding: ActivityNewUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        newUserBinding = ActivityNewUserBinding.inflate(layoutInflater)
        val view = newUserBinding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        newUserBinding.NewSignUpButton.setOnClickListener {
            val email = newUserBinding.emailedittext.text.toString()
            val password = newUserBinding.passwordedittext.text.toString()
            signupfirebase(email,password)
        }
    }

    fun signupfirebase(email : String,password: String){
        newUserBinding.NewSignUpButton.isClickable = false
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Sign Up Successful",Toast.LENGTH_SHORT).show()
                finish()
                newUserBinding.NewSignUpButton.isClickable = true
            }
            else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }
}