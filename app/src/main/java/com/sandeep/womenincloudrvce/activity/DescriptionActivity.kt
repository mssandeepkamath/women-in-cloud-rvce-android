package com.sandeep.womenincloudrvce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.sandeep.womenincloudrvce.databinding.ActivityEventDescriptionBinding
import com.sandeep.womenincloudrvce.databinding.ActivityInternshipDescriptionBinding
import com.sandeep.womenincloudrvce.databinding.ActivityProjectDescriptionBinding
import com.sandeep.womenincloudrvce.R
import com.sandeep.womenincloudrvce.entity.Event
import com.sandeep.womenincloudrvce.entity.Internship
import com.sandeep.womenincloudrvce.entity.Project
import com.sandeep.womenincloudrvce.util.RequestQueueSingleton
import com.google.firebase.auth.FirebaseAuth
import com.thecode.aestheticdialogs.*
import org.json.JSONException
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    private lateinit var bindingProjectDescriptionActivity: ActivityProjectDescriptionBinding
    private lateinit var bindingInternshipDescriptionBinding: ActivityInternshipDescriptionBinding
    private lateinit var bindingEventDescriptionBinding: ActivityEventDescriptionBinding
    private lateinit var auth : FirebaseAuth
    private var usn="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProjectDescriptionActivity =
            ActivityProjectDescriptionBinding.inflate(layoutInflater)
        bindingInternshipDescriptionBinding =
            ActivityInternshipDescriptionBinding.inflate(layoutInflater)
        bindingEventDescriptionBinding = ActivityEventDescriptionBinding.inflate(layoutInflater)

        bindingProjectDescriptionActivity.ivBack.setOnClickListener {
            finish()
        }
        bindingInternshipDescriptionBinding.ivBack.setOnClickListener {
            finish()
        }
        bindingEventDescriptionBinding.ivBack.setOnClickListener {
            finish()
        }

        val project: Project? = intent.getSerializableExtra("PROJECT") as Project?
        val internship: Internship? = intent.getSerializableExtra("INTERNSHIP") as Internship?
        val event: Event? = intent.getSerializableExtra("EVENT") as Event?
        auth = FirebaseAuth.getInstance()
        val email_id = auth.currentUser?.email
        getUsn(email_id)
        if (project != null) {
            super.setContentView(bindingProjectDescriptionActivity.root)
            bindingProjectDescriptionActivity.tvName.text = project.company_name
            bindingProjectDescriptionActivity.tvVacancy.text = project.opening.substring(8)
            bindingProjectDescriptionActivity.tvStart.text = project.start_date
            bindingProjectDescriptionActivity.tvEnd.text = project.end_date
            bindingProjectDescriptionActivity.tvDescription.text = project.description.substring(13)
            bindingProjectDescriptionActivity.tvRequirement.text = project.requirements
            bindingProjectDescriptionActivity.tvManager.text = project.manager
            bindingProjectDescriptionActivity.tvResources.text = project.resources
            bindingProjectDescriptionActivity.btnApply.setOnClickListener {
                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
                val url = resources.getString(R.string.applyProject)
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("USN", usn)
                    jsonObject.put("project_id", project.id)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                volleyHttpPost(jsonObject, url)
            }
        } else if (internship != null) {
            super.setContentView(bindingInternshipDescriptionBinding.root)
            bindingInternshipDescriptionBinding.tvName.text = internship.company_name
            bindingInternshipDescriptionBinding.tvVacancy.text = internship.opening.substring(8)
            bindingInternshipDescriptionBinding.tvStart.text = internship.start_date
            bindingInternshipDescriptionBinding.tvEnd.text = internship.end_date
            bindingInternshipDescriptionBinding.tvDescription.text =
                internship.role_description.substring(18)
            bindingInternshipDescriptionBinding.tvRequirement.text = internship.requirements
            bindingInternshipDescriptionBinding.tvManager.text = internship.manager
            bindingInternshipDescriptionBinding.tvLocation.text = internship.location
            bindingInternshipDescriptionBinding.tvMode.text = "Mode: " + internship.mode + "\t\t\t\tType: " + internship.type
            bindingInternshipDescriptionBinding.btnApply.setOnClickListener {
                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
                val url = resources.getString(R.string.applyInternship)
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("USN", usn)
                    jsonObject.put("internship_id", internship.id)
                    println("input: ${jsonObject.toString()}")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                volleyHttpPost(jsonObject, url)
            }
        } else if(event!=null) {
            super.setContentView(bindingEventDescriptionBinding.root)
            bindingEventDescriptionBinding.tvName.text = event.name
            bindingEventDescriptionBinding.tvStart.text = event.start_date
            bindingEventDescriptionBinding.tvEnd.text = event.end_date
            bindingEventDescriptionBinding.tvDescription.text =
                event.description.substring(13)
            bindingEventDescriptionBinding.tvLocation.text = event.location.substring(7)
            bindingEventDescriptionBinding.tvMode.text = "Type: " + event.type
            bindingEventDescriptionBinding.btnApply.setOnClickListener {
                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
                val url = resources.getString(R.string.applyEvent)
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("USN", usn)
                    jsonObject.put("event_id", event.id)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                volleyHttpPost(jsonObject, url)
            }
        }
    }

    fun volleyHttpPost(jsonObject: JSONObject,url : String) {
        var statusCode: Int = 0
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            {

            },{
                println(statusCode)
                when(statusCode) {
                    200 -> {
                        var content = if(url.contains("apply-event")) {
                            "Registration successful, Thank you for your interest in WIC."
                        } else {
                            "Your application has been submitted. We will get back to you after reviewing the same."
                        }
                        AestheticDialog.Builder(this@DescriptionActivity,
                            DialogStyle.FLASH,
                            DialogType.SUCCESS)
                            .setTitle("Congratulations")
                            .setMessage(content)
                            .setCancelable(false)
                            .setDarkMode(true)
                            .setGravity(Gravity.CENTER)
                            .setAnimation(DialogAnimation.SHRINK)
                            .setOnClickListener(object : OnDialogClickListener {
                                override fun onClick(dialog: AestheticDialog.Builder) {
                                    dialog.dismiss()
                                }
                            })
                            .show()
                    }
                    500 -> {
                        var content = if(url.contains("apply-event")) {
                            "Registration failed, Please try again or email us the issue"
                        } else {
                            "Sorry, You have a ongoing Project/Internship and we do not allow multiple applications. Feel free to email us."
                        }
                        AestheticDialog.Builder(this@DescriptionActivity,
                            DialogStyle.FLASH,
                            DialogType.ERROR)
                            .setTitle("Attention")
                            .setMessage(content)
                            .setCancelable(false)
                            .setDarkMode(true)
                            .setGravity(Gravity.CENTER)
                            .setAnimation(DialogAnimation.SHRINK)
                            .setOnClickListener(object : OnDialogClickListener {
                                override fun onClick(dialog: AestheticDialog.Builder) {
                                    dialog.dismiss()
                                }
                            })
                            .show()
                    }
                }

            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                headers["Content-Type"] = "application/json"
                return headers
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                statusCode = response!!.statusCode
                return super.parseNetworkResponse(response)
            }

            override fun parseNetworkError(volleyError: VolleyError?): VolleyError {
                statusCode = volleyError!!.networkResponse!!.statusCode
                return super.parseNetworkError(volleyError)
            }
        }
        RequestQueueSingleton.getInstance(applicationContext)
            .addToRequestQueue(jsonObjectRequest)
    }

    fun getUsn(email :String?)
    {
        val request = JsonObjectRequest(
            Request.Method.GET, "https://springbootapi-production-c1e0.up.railway.app/usn/${email}",null,
            {
                usn = it.getString("USN")
            },{
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            })
        RequestQueueSingleton.getInstance(applicationContext)
            .addToRequestQueue(request)
    }
}