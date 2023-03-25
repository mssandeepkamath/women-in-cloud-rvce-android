package com.example.tripfactory_concierge_android.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.databinding.ActivityRegisterBinding
import com.example.tripfactory_concierge_android.firebase.FirestoreClass
import com.example.tripfactory_concierge_android.models.User
import com.example.tripfactory_concierge_android.ui.activities.MainActivity
import com.example.tripfactory_concierge_android.util.RequestQueueSingleton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var authh: FirebaseAuth
    private lateinit var bindingRegisterActivity: ActivityRegisterBinding
    private var flag1 = false;
    private var flag2 = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingRegisterActivity = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bindingRegisterActivity.root)
        authh= FirebaseAuth.getInstance()

        bindingRegisterActivity.haveAcc.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        bindingRegisterActivity.show.setOnClickListener {
            if (flag1==false) {
                bindingRegisterActivity.passwordcon.transformationMethod = HideReturnsTransformationMethod.getInstance()
                flag1=true
            } else {
                bindingRegisterActivity.passwordcon.transformationMethod = PasswordTransformationMethod.getInstance()
                flag1=false
            }
        }
        bindingRegisterActivity.showre.setOnClickListener {
            if (flag2==false) {
                bindingRegisterActivity.repasswordcon.transformationMethod = HideReturnsTransformationMethod.getInstance()
                flag2=true
            } else {
                bindingRegisterActivity.repasswordcon.transformationMethod = PasswordTransformationMethod.getInstance()
                flag2=false
            }
        }
        bindingRegisterActivity.cardSignIn.setOnClickListener {
            val password:String=bindingRegisterActivity.passwordcon.text.toString()
            val email=bindingRegisterActivity.emailcon.text.toString()
            val repassword:String=bindingRegisterActivity.repasswordcon.text.toString()
            val namee:String= bindingRegisterActivity.namecon.text.toString().trim().lowercase(Locale.getDefault())
            val phonenum=bindingRegisterActivity.phonecon.text.toString().trim()
            val USN =bindingRegisterActivity.addresscon.text.toString()
            val batch=bindingRegisterActivity.batchicon.text.toString()
            val dept=email[email.indexOf(".")+1].toString()+email[email.indexOf(".")+2].toString()


            if(namee.isEmpty() || USN.isEmpty() || phonenum.isEmpty() || email.isEmpty() || password.isEmpty() || repassword.isEmpty() || batch.isEmpty())
            {
                Toast.makeText(this,"Fields cannot be left empty",Toast.LENGTH_LONG).show()
            }
            else if (password != repassword) {
                Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_LONG).show()
            }
            else if(phonenum.length!=10)
            {
                Toast.makeText(this,"Enter valid phone number",Toast.LENGTH_LONG).show()
            }
            else if(!email.contains("@rvce.edu.in",true))
            {
                Toast.makeText(this, "Enter RVCE email id", Toast.LENGTH_LONG).show()
            }
            else {

                Toast.makeText(this, "Please wait...", Toast.LENGTH_LONG).show()

                authh.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                           val jsonObject = JSONObject()
                            try {
                                jsonObject.put("usn", USN)
                                jsonObject.put("batch", batch)
                                jsonObject.put("department",dept)
                                jsonObject.put("email_id", email)
                                jsonObject.put("student_first_name", namee.substringBeforeLast(" ").toString())
                                jsonObject.put("phone_number", phonenum)
                                jsonObject.put("student_last_name",namee.substringAfterLast(" ").toString())
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            val url = resources.getString(R.string.register_user)
                            val jsonObjectRequest = JsonObjectRequest(
                                Request.Method.POST, url, jsonObject,
                                {

                                },{
                                    val userprofilename= UserProfileChangeRequest.Builder()
                                        .setDisplayName(namee)
                                        .build()
                                    val user = User(authh.currentUser!!.uid, namee,email)
                                    FirestoreClass().registerUser(this, user)
                                    authh.currentUser!!.updateProfile(userprofilename)
                                    Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                })
                            RequestQueueSingleton.getInstance(applicationContext)
                                .addToRequestQueue(jsonObjectRequest)
                        }
                        else
                        {
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }

    }

    fun signInSuccess(loggedInUser: User?) {
        if(loggedInUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    fun showErrorSnackBar(message: String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error_color))
        snackBar.show()
    }



}