package com.example.tripfactory_concierge_android.activity

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.databinding.ActivitySplashBinding
import com.example.tripfactory_concierge_android.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    lateinit var screen: ViewGroup
    private lateinit var auth: FirebaseAuth
    private lateinit var bindingSplashActivity: ActivitySplashBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplashActivity= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bindingSplashActivity.root)
        auth = FirebaseAuth.getInstance()
        val current_user = auth.currentUser

        android.os.Handler().postDelayed(
            {
                if (current_user != null) {
                    if (ConnectionManager().checkConnectivity(this) == true)
                        intentProvider(MainActivity())
                    else
                        ConnectionManager().createDialog(bindingSplashActivity.bgSplashActivity, this)
                } else {
                    intentProvider(LoginActivity())
                }

            }, 1500)
    }

    private fun intentProvider(activity: Activity) {
        val intent=Intent(this,activity::class.java)
        startActivity(intent)
        finish()
    }

}