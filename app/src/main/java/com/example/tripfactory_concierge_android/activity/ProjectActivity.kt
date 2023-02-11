package com.example.tripfactory_concierge_android.activity

import android.content.Context
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
import com.android.volley.toolbox.JsonObjectRequest
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.adapter.ProjectAdapter
import com.example.tripfactory_concierge_android.databinding.ActivityProjectBinding
import com.example.tripfactory_concierge_android.entity.Project
import com.example.tripfactory_concierge_android.util.ConnectionManager
import com.example.tripfactory_concierge_android.util.RequestQueueSingleton
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ProjectActivity : AppCompatActivity() {

    lateinit var itemArray: ArrayList<Project>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var bindingProjectActivity: ActivityProjectBinding
    lateinit var auth: FirebaseAuth
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProjectActivity = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(bindingProjectActivity.root)
        var runnable: Runnable? = null
        layoutManager = LinearLayoutManager(this)
        bindingProjectActivity.lytRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green),
            ContextCompat.getColor(this, R.color.red)
        );
        bindingProjectActivity.lytProgress.visibility = View.VISIBLE
        bindingProjectActivity.txtError.visibility = View.GONE
        auth = FirebaseAuth.getInstance()

        itemArray = ArrayList()
        volleyGetRequest()
        bindingProjectActivity.lytRefresh.setOnRefreshListener(object :
            SwipeRefreshLayout.OnRefreshListener {

            override fun onRefresh() {
                itemArray.clear()
                bindingProjectActivity.vwRecyclerView.adapter?.notifyDataSetChanged()
                bindingProjectActivity.lytProgress.visibility = View.VISIBLE
                bindingProjectActivity.barProgress.visibility = View.VISIBLE
                bindingProjectActivity.txtError.visibility = View.GONE
                Handler().postDelayed(
                    Runnable {

                        bindingProjectActivity.lytRefresh.isRefreshing = false
                        volleyGetRequest()
                    }, 1000
                )
            }
        })

    }

    fun volleyGetRequest() {
        val url = resources.getString(R.string.projectsList)
        if (ConnectionManager().checkConnectivity(this)) {
            val jsonObjectRequest = object : JsonArrayRequest(
                Method.GET,
                url,
                null,
                { response ->
                    try {
                        val jsonArray: JSONArray = response

                        bindingProjectActivity.lytProgress.visibility = View.GONE
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val company_name: String = jsonObject.getString("company_name")
                            val opening: String ="Vacancy: " + jsonObject.getInt("opening").toString()
                            val description: String = "Description: " + jsonObject.getString("description")
                            val requirements: String = jsonObject.getString("requirements")
                            val start_date: String = jsonObject.getString("start_date")
                            val end_date: String = jsonObject.getString("end_date")
                            val resources: String = jsonObject.getString("resources")
                            val manager: String = jsonObject.getString("manager")
                            itemArray.add(
                                Project(
                                    company_name,
                                    opening,
                                    description,
                                    requirements,
                                    manager,
                                    start_date,
                                    end_date,
                                    resources
                                )
                            )

                        }
                        bindingProjectActivity.vwRecyclerView.layoutManager = layoutManager
                        bindingProjectActivity.vwRecyclerView.adapter = ProjectAdapter(this, itemArray)
                    } catch (e: JSONException) {
                        bindingProjectActivity.lytProgress.visibility = View.VISIBLE
                        bindingProjectActivity.barProgress.visibility = View.GONE
                        bindingProjectActivity.txtError.visibility = View.VISIBLE
                        ToastMessage("Some unexpected error occurred.")
                    }

                },
                { error ->
                    bindingProjectActivity.barProgress.visibility = View.GONE
                    bindingProjectActivity.txtError.visibility = View.VISIBLE
                    ToastMessage("Something went wrong..")
                    println("Error: $error")
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
            RequestQueueSingleton.getInstance(applicationContext)
                .addToRequestQueue(jsonObjectRequest)

        } else {
            bindingProjectActivity.barProgress.visibility = View.GONE
            bindingProjectActivity.txtError.visibility = View.VISIBLE
            ConnectionManager().createDialog(bindingProjectActivity.lytRel, this)
        }

    }

    fun ToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
