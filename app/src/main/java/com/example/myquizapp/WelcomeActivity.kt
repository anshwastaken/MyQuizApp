package com.example.myquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.myquizapp.databinding.ActivityWelcomeBinding
import kotlinx.coroutines.Runnable

class WelcomeActivity : AppCompatActivity() {
    lateinit var splashbinding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        splashbinding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = splashbinding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        val aplhanimation = AnimationUtils.loadAnimation(applicationContext,R.anim.splash_anim)
        splashbinding.QuizGameText.startAnimation(aplhanimation)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable{
            override fun run() {
                val intent = Intent(this@WelcomeActivity,LoginActivity::class.java)
                startActivity(intent)
            }

        },5000)
    }
}