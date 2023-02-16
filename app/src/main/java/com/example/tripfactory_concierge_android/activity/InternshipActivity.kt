package com.example.tripfactory_concierge_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonArrayRequest
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.adapter.InternshipAdapter
import com.example.tripfactory_concierge_android.adapter.ProjectAdapter
import com.example.tripfactory_concierge_android.databinding.ActivityInternshipBinding
import com.example.tripfactory_concierge_android.entity.Internship
import com.example.tripfactory_concierge_android.entity.Project
import com.example.tripfactory_concierge_android.util.ConnectionManager
import com.example.tripfactory_concierge_android.util.RequestQueueSingleton
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class InternshipActivity : AppCompatActivity() {
    lateinit var itemArray: ArrayList<Internship>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var bindingProjectActivity: ActivityInternshipBinding
    lateinit var auth: FirebaseAuth
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProjectActivity = ActivityInternshipBinding.inflate(layoutInflater)
        setContentView(bindingProjectActivity.root)
        bindingProjectActivity.ivBack.setOnClickListener {
            finish()
        }
        layoutManager = LinearLayoutManager(this)
        bindingProjectActivity.lytRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green),
            ContextCompat.getColor(this, R.color.red)
        );
        bindingProjectActivity.lytProgress.visibility = View.VISIBLE
       bindingProjectActivity.barProgress.startRippleAnimation()
        bindingProjectActivity.txtError.visibility = View.GONE
        auth = FirebaseAuth.getInstance()

        itemArray = ArrayList()
        volleyGetRequest()
        bindingProjectActivity.lytRefresh.setOnRefreshListener(object :
            SwipeRefreshLayout.OnRefreshListener {

            override fun onRefresh() {
                bindingProjectActivity.vwRecyclerView.adapter?.notifyDataSetChanged()
//                bindingProjectActivity.lytProgress.visibility = View.VISIBLE
//                bindingProjectActivity.barProgress.visibility = View.VISIBLE
//                bindingProjectActivity.txtError.visibility = View.GONE
                volleyGetRequest()
            }
        })

    }

    fun volleyGetRequest() {
        val url = resources.getString(R.string.internshipList)
        if (ConnectionManager().checkConnectivity(this)) {
            val jsonObjectRequest = object : JsonArrayRequest(
                Method.GET,
                url,
                null,
                { response ->
                    try {
                        val jsonArray: JSONArray = response
                        bindingProjectActivity.lytProgress.visibility = View.GONE
                        itemArray.clear()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val id :Int=jsonObject.getInt("internship_id")
                            val company_name: String = jsonObject.getString("company_name")
                            val opening: String =
                                "Vacancy: " + jsonObject.getInt("opening").toString()
                            val description: String =
                                "Role description: " + jsonObject.getString("role_description").uppercase(Locale.getDefault())
                            val requirements: String = jsonObject.getString("requirements")
                            val start_date: String ="Start: "+ jsonObject.getString("start_date").substring(0,10)
                            val end_date: String = "End: "+jsonObject.getString("end_date").substring(0,10)
                            val manager: String = jsonObject.getString("manager")
                            val mode: String = jsonObject.getString("mode")
                                .uppercase(Locale.getDefault())
                            val type: String = jsonObject.getString("type").uppercase(Locale.getDefault())
                            println("Check point 3")
                            val location: String = jsonObject.getString("location")
                            itemArray.add(
                                Internship(
                                    id,
                                    company_name,
                                    opening,
                                    description,
                                    requirements,
                                    manager,
                                    start_date,
                                    end_date,
                                    mode,
                                    type,
                                    location
                                )
                            )

                            println("Check point 4")

                        }
                        bindingProjectActivity.vwRecyclerView.layoutManager = layoutManager
                        bindingProjectActivity.vwRecyclerView.adapter =
                            InternshipAdapter(this, itemArray)
                        bindingProjectActivity.lytRefresh.isRefreshing = false
                    } catch (e: JSONException) {
                        bindingProjectActivity.lytProgress.visibility = View.VISIBLE
                        bindingProjectActivity.barProgress.visibility = View.GONE
                        bindingProjectActivity.barProgress.stopRippleAnimation()
                        bindingProjectActivity.txtError.visibility = View.VISIBLE
                        ToastMessage("Some unexpected error occurred.")
                        println(e)
                    }

                },
                { error ->
                    bindingProjectActivity.barProgress.visibility = View.GONE
                    bindingProjectActivity.barProgress.stopRippleAnimation()
                    bindingProjectActivity.txtError.visibility = View.VISIBLE
                    ToastMessage("Something went wrong..")
                    println("Error: $error")
                }
            )
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }

            // add network request to volley queue
            RequestQueueSingleton.getInstance(applicationContext)
                .addToRequestQueue(jsonObjectRequest)

        } else {
            bindingProjectActivity.barProgress.visibility = View.GONE
            bindingProjectActivity.barProgress.stopRippleAnimation()
            bindingProjectActivity.txtError.visibility = View.VISIBLE
            ConnectionManager().createDialog(bindingProjectActivity.lytRel, this)
        }

    }

    fun ToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}