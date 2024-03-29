package com.sandeep.womenincloudrvce.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.sandeep.womenincloudrvce.databinding.ActivitySplashBinding
import com.sandeep.womenincloudrvce.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    lateinit var screen: ViewGroup
    private lateinit var auth: FirebaseAuth
    private lateinit var bindingSplashActivity: ActivitySplashBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplashActivity= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bindingSplashActivity.root)
        bindingSplashActivity.lytripple.startRippleAnimation()
        auth = FirebaseAuth.getInstance()
        val current_user = auth.currentUser

        android.os.Handler().postDelayed(
            {
                bindingSplashActivity.lytripple.stopRippleAnimation()
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