package com.example.livioreinoso1165606.firebase_demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    lateinit var loading : ProgressBar
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth


        println(auth.currentUser?.uid)

        val emailField = findViewById<TextView>(R.id.email)
        val passField = findViewById<TextView>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loading = findViewById(R.id.spinner)

        loginBtn.setOnClickListener {
            if(emailField.text.isNotEmpty() && passField.text.isNotEmpty()){
                val email = emailField.text.toString().trim()
                val pass = passField.text.toString().trim()

                loading.visibility = View.VISIBLE
                loginUser(auth, email, pass)

            }
        }

    }

    private fun loginUser(auth: FirebaseAuth, email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                loading.visibility = View.GONE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Firebase", "signInWithEmailAndPassword:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Welcome!",
                        Toast.LENGTH_LONG
                    ).show()
                    countryRoadsTakeMeHome()
                } else {
                    println(task.exception)
                    // If sign in fails, display a message to the user.
                    Log.w("Firebase", "signInWithEmailAndPassword:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
    }
    private fun countryRoadsTakeMeHome(){
        val intent = Intent(this, WelcomeActivity::class.java)
        val bundle = Bundle()
        bundle.putString("uid",auth.currentUser?.uid)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            countryRoadsTakeMeHome()
        }
    }

}