package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myquizapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var googleSignInClient: GoogleSignInClient
    val auth = FirebaseAuth.getInstance()
    lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        val googletext = loginBinding.googleSignIn.getChildAt(0) as TextView
        googletext.text = "Continue with google"
        googletext.setTextColor(Color.BLACK)
        googletext.textSize = 18F

        regiteractivitygooglesignin()

        loginBinding.signinButton.setOnClickListener {
            val email = loginBinding.emailinputedit.text.toString()
            val password = loginBinding.passwordinputedit.text.toString()

            SignInFirebase(email,password)
        }

        loginBinding.googleSignIn.setOnClickListener {
            googleSignIn()
        }

        loginBinding.signUpText.setOnClickListener {
            val intent = Intent(this@LoginActivity,NewUserActivity::class.java)
            startActivity(intent)
        }

        loginBinding.forgotpasstext.setOnClickListener {
            val intent = Intent(this@LoginActivity,ForgotActivity::class.java)
            startActivity(intent)
        }
    }

    fun SignInFirebase(email : String,Password : String){

        auth.signInWithEmailAndPassword(email,Password).addOnCompleteListener { task->
            if (task.isSuccessful){
                Toast.makeText(applicationContext,"Successfully Signed In",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user!=null){
            Toast.makeText(applicationContext,"Successfully Signed In",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun googleSignIn(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("634439362137-p46sprnpd9qqf84p34ritgagad1vi9fa.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        SignIn()
    }

    fun SignIn(){
        val signInIntent : Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    fun regiteractivitygooglesignin(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultcode = result.resultCode
                val data = result.data

                if(resultcode == RESULT_OK && data != null){
                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebasesigninGoogle(task)
                }
            })
    }

    fun firebasesigninGoogle(task : Task<GoogleSignInAccount>){
        try{
            val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext,"Welcome to Quiz Game",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }catch (e : ApiException){
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }

    fun firebaseGoogleAccount(account: GoogleSignInAccount){
        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->

            if (task.isSuccessful){

                //val user = auth.currentUser

            }else{

            }

        }

    }
    }
