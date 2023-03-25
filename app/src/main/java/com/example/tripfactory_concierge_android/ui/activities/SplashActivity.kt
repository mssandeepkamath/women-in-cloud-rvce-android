package com.example.tripfactory_concierge_android.ui.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.databinding.ActivitySplashBinding
import com.example.tripfactory_concierge_android.databinding.ActivitySplashTrelloBinding
import com.example.tripfactory_concierge_android.firebase.FirestoreClass
import com.example.tripfactory_concierge_android.models.User
import com.google.firebase.auth.FirebaseAuth



class SplashActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var activitySplashTrelloBinding:  ActivitySplashTrelloBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        activitySplashTrelloBinding=ActivitySplashTrelloBinding.inflate(layoutInflater)
        setContentView(activitySplashTrelloBinding.root)
        auth=FirebaseAuth.getInstance()
        activitySplashTrelloBinding.lytripple.startRippleAnimation()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        FirestoreClass().loadUserData(this)

    }

    fun signInSuccess(loggedInUser: User?) {
        if(loggedInUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else {
            val user = User(auth.currentUser!!.uid,  auth.currentUser!!.email!!.substringBefore(".").toString(), auth.currentUser!!.email)
            FirestoreClass().registerUser(this, user)
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,com.example.tripfactory_concierge_android.activity.MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}