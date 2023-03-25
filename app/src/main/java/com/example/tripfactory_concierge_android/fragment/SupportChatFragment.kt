package com.example.tripfactory_concierge_android.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.activity.MainActivity
import com.example.tripfactory_concierge_android.databinding.ActivityMainBinding
import com.example.tripfactory_concierge_android.databinding.FragmentSupportChatBinding
import com.example.tripfactory_concierge_android.ui.activities.SplashActivity
import com.example.tripfactory_concierge_android.util.ConnectionManager
import com.example.tripfactory_concierge_android.util.RequestQueueSingleton
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException


class SupportChatFragment : Fragment(), OnClickListener {

    private lateinit var bindingSupportChat: FragmentSupportChatBinding
    private lateinit var bindingMainActivity: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingSupportChat = FragmentSupportChatBinding.inflate(inflater, container, false)
        bindingMainActivity = (activity as MainActivity).bindingMainActivity
        bindingSupportChat.progressValidation.visibility=View.VISIBLE
        //on Click calls
        bindingSupportChat.ivBack.setOnClickListener(this)
        bindingSupportChat.ivHamburger.setOnClickListener(this)
        bindingSupportChat.cvEmail.setOnClickListener(this)
        auth=FirebaseAuth.getInstance()
        try {
            volleyGetRequest()
        }catch (e:java.lang.Exception) {
            println(e.message)
        }

        return bindingSupportChat.root
    }

    override fun onClick(v: View?) {        when (v?.id) {
            //Open drawer
            R.id.ivHamburger -> bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
            //Go back to home fragment
            R.id.ivBack -> {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(bindingMainActivity.lytFrame.id, HomeFragment(), "HomeFragment")
                    .commit()
                bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.home).isChecked = true
            }

            R.id.cvEmail -> {
                //TODO
                val to = "msandeepcip@gmail.com"
                val subject = "NEED SUPPORT"
                val body =
                    "Please clearly mention your concern."
                val mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body)
                val emailIntent = Intent(Intent.ACTION_VIEW)
                emailIntent.data = Uri.parse(mailTo)
                (activity as MainActivity).startActivity(emailIntent)
            }
        }
    }
    fun volleyGetRequest(){
        val url = resources.getString(R.string.usn_from_email)+auth.currentUser!!.email
        if (ConnectionManager().checkConnectivity(activity as MainActivity)) {
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET,
                url,
                null,
                { response ->
                    try {
                        val url = resources.getString(R.string.student)
                        val jsonObjectRequest = JsonObjectRequest(
                            Request.Method.POST, url, response,
                            {
                                try {
                                    if(!it.isNull("on_going_project") || !it.isNull("on_going_internship"))
                                    {
                                        val intent = Intent(activity as MainActivity, SplashActivity::class.java)
                                        (activity as MainActivity).startActivity(intent)
                                        (activity as MainActivity).finish()
                                    }
                                    else
                                    {
                                        bindingSupportChat.progressValidation.visibility = View.GONE
                                        bindingSupportChat.validationText.setTextColor(resources.getColor(R.color.red))
                                        bindingSupportChat.validationText.text="Access denied!, You need to be a part of WIC project/internship to access Workloud"
                                    }
                                }
                                catch (e: Exception) {
                                    println(e.message)
                                }
                            },{
                                Toast.makeText(activity as MainActivity,"Error from our side, try again!",Toast.LENGTH_SHORT).show()
                            })
                        RequestQueueSingleton.getInstance((activity as MainActivity).applicationContext)
                            .addToRequestQueue(jsonObjectRequest)

                    } catch (e: Exception) {
                        println(e.message)
                    }

                },
                { error ->
                    Toast.makeText(activity as MainActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }

            // add network request to volley queue
            RequestQueueSingleton.getInstance((activity as MainActivity).applicationContext)
                .addToRequestQueue(jsonObjectRequest)

        } else {

            ConnectionManager().createDialog(bindingSupportChat.ivBack, activity as MainActivity)
        }

    }

}