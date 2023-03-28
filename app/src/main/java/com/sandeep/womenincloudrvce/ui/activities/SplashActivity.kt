package com.sandeep.womenincloudrvce.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.sandeep.womenincloudrvce.databinding.ActivitySplashTrelloBinding
import com.sandeep.womenincloudrvce.firebase.FirestoreClass
import com.sandeep.womenincloudrvce.models.User
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
        val intent = Intent(this,com.sandeep.womenincloudrvce.activity.MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}