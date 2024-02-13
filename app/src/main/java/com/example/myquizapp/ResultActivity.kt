package com.example.myquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myquizapp.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding: ActivityResultBinding
    val database = FirebaseDatabase.getInstance()
    val myref = database.reference.child("scores")
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var correct = ""
    var wrong = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        myref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user?.let {
                    val userUID = it.uid
                    correct = snapshot.child(userUID).child("correct").value.toString()
                    wrong = snapshot.child(userUID).child("wrong").value.toString()

                    resultBinding.correctAnswerText.text = correct
                    resultBinding.WrongAnswerText.text = wrong
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        resultBinding.ExitButton.setOnClickListener {
            finish()
        }

        resultBinding.PlayAgainButton.setOnClickListener {
            val intent = Intent(this@ResultActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}