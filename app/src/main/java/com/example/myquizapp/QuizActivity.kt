package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myquizapp.databinding.ActivityQuizBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    val database = Firebase.database
    val myref = database.reference.child("Questions")
    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correct_answer = ""
    var QuestionCount = 0
    var QuestionNumber = 1
    var userAnswer = ""
    var coorectanswerNo = 0
    var wronganswerNo = 0
    lateinit var timer: CountDownTimer
    var totaltime = 25000L
    var timeContinue = false
    var leftime = totaltime

    var auth = FirebaseAuth.getInstance()
    var user = auth.currentUser
    var scoreref = database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        GameLogic()
        
        quizBinding.OptionText1.setOnClickListener {
            userAnswer = "a"
            if(correct_answer == userAnswer){
                quizBinding.OptionText1.setBackgroundColor(Color.GREEN)
                coorectanswerNo++
                quizBinding.correctAnswerNumber.text = coorectanswerNo.toString()
            }
            else{
                quizBinding.OptionText1.setBackgroundColor(Color.RED)
                wronganswerNo++
                quizBinding.correctAnswerNumber.text = wronganswerNo.toString()
                findanswer()
            }
            disableclick()
        }

        quizBinding.OptionText2.setOnClickListener {
            userAnswer = "b"
            if(correct_answer == userAnswer){
                quizBinding.OptionText2.setBackgroundColor(Color.GREEN)
                coorectanswerNo++
                quizBinding.correctAnswerNumber.text = coorectanswerNo.toString()
            }
            else{
                quizBinding.OptionText2.setBackgroundColor(Color.RED)
                wronganswerNo++
                quizBinding.correctAnswerNumber.text = wronganswerNo.toString()
                findanswer()
            }
            disableclick()
        }

        quizBinding.OptionText3.setOnClickListener {
            userAnswer = "c"
            if(correct_answer == userAnswer){
                quizBinding.OptionText3.setBackgroundColor(Color.GREEN)
                coorectanswerNo++
                quizBinding.correctAnswerNumber.text = coorectanswerNo.toString()
            }
            else{
                quizBinding.OptionText3.setBackgroundColor(Color.RED)
                wronganswerNo++
                quizBinding.correctAnswerNumber.text = wronganswerNo.toString()
                findanswer()
            }
            disableclick()
        }

        quizBinding.OptionText4.setOnClickListener {
            userAnswer = "d"
            if(correct_answer == userAnswer){
                quizBinding.OptionText4.setBackgroundColor(Color.GREEN)
                coorectanswerNo++
                quizBinding.correctAnswerNumber.text = coorectanswerNo.toString()
            }
            else{
                quizBinding.OptionText4.setBackgroundColor(Color.RED)
                wronganswerNo++
                quizBinding.correctAnswerNumber.text = wronganswerNo.toString()
                findanswer()
            }
            disableclick()
        }

        quizBinding.FinishText.setOnClickListener {
            SendScore()
        }

    }

    private fun GameLogic(){
        restoreOptions()
        myref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                QuestionCount = snapshot.childrenCount.toInt()

                if(QuestionNumber <= QuestionCount){
                    question = snapshot.child(QuestionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(QuestionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(QuestionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(QuestionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(QuestionNumber.toString()).child("d").value.toString()
                    correct_answer = snapshot.child(QuestionNumber.toString()).child("answer").value.toString()

                    quizBinding.QuestionText.text = question
                    quizBinding.OptionText1.text = answerA
                    quizBinding.OptionText2.text = answerB
                    quizBinding.OptionText3.text = answerC
                    quizBinding.OptionText4.text = answerD
                }
                else{
                    val dialogbox = AlertDialog.Builder(this@QuizActivity)
                    dialogbox.setTitle("Quiz Game")
                    dialogbox.setMessage("Congratulations \n U have answered all the questions!! U wanna see the result")
                    dialogbox.setCancelable(false)
                    dialogbox.setPositiveButton("See Result"){dialogmessage,position ->
                        SendScore()
                    }

                    dialogbox.setNegativeButton("Play Again"){dialogmessage,position ->
                        val intent = Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogbox.create().show()
                }
                QuestionNumber++
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun findanswer(){
        when(correct_answer){
            "a" -> quizBinding.OptionText1.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.OptionText2.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.OptionText3.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.OptionText4.setBackgroundColor(Color.GREEN)

        }
    }

    private fun disableclick(){
        quizBinding.OptionText1.isClickable = false
        quizBinding.OptionText2.isClickable = false
        quizBinding.OptionText3.isClickable = false
        quizBinding.OptionText4.isClickable = false
    }

    private fun restoreOptions(){
        quizBinding.OptionText1.setBackgroundColor(Color.WHITE)
        quizBinding.OptionText2.setBackgroundColor(Color.WHITE)
        quizBinding.OptionText3.setBackgroundColor(Color.WHITE)
        quizBinding.OptionText4.setBackgroundColor(Color.WHITE)

        quizBinding.OptionText1.isClickable = true
        quizBinding.OptionText2.isClickable = true
        quizBinding.OptionText3.isClickable = true
        quizBinding.OptionText4.isClickable = true
    }

    private fun timeleft(){
        timer = object : CountDownTimer(leftime,1000){
            override fun onTick(millisUntilFinished: Long) {
                leftime = millisUntilFinished
                updateCountDown()
            }

            override fun onFinish() {
                resetTimer()
                updateCountDown()
                Toast.makeText(applicationContext,"Sorry continue with next question",Toast.LENGTH_SHORT).show()
                timeContinue = false
            }

        }.start()
        timeContinue = true
    }

    fun SendScore(){
        user?.let {
            val useruid = it.uid
            scoreref.child("scores").child(useruid).child("Correct").setValue(coorectanswerNo)
            scoreref.child("scores").child(useruid).child("Wrong").setValue(wronganswerNo).addOnCompleteListener {
                Toast.makeText(applicationContext,"Quiz is Finished",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}