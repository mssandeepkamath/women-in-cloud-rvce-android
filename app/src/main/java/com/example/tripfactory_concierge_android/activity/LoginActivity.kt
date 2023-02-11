package com.example.tripfactory_concierge_android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.databinding.ActivityLoginBinding
import com.example.tripfactory_concierge_android.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var bindingLoginActivity: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    private var flag1=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLoginActivity = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLoginActivity.root)
        auth= FirebaseAuth.getInstance()

        bindingLoginActivity.show.setOnClickListener {
            if (flag1==false) {
                bindingLoginActivity.passwordTest.transformationMethod = HideReturnsTransformationMethod.getInstance()
                flag1=true
            } else {
                bindingLoginActivity.passwordTest.transformationMethod = PasswordTransformationMethod.getInstance()
                flag1=false
            }
        }

        bindingLoginActivity.cardSignIn.setOnClickListener {
            val email:String = bindingLoginActivity.emailIdTest.text.toString().trim().lowercase(Locale.getDefault())
            val password:String=bindingLoginActivity.passwordTest.text.toString()


            if(email.isEmpty() || password.isEmpty())
            {
                Toast.makeText(this,"Fields cannot be left empty",Toast.LENGTH_LONG).show()
            }else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Welcome to COE", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }

                    }
            }
        }

        bindingLoginActivity.privilegedAccess.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}