package com.sandeep.womenincloudrvce.activity

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
import com.sandeep.womenincloudrvce.R
import com.sandeep.womenincloudrvce.adapter.EventAdapter
import com.sandeep.womenincloudrvce.databinding.ActivityEventBinding
import com.sandeep.womenincloudrvce.entity.Event
import com.sandeep.womenincloudrvce.util.ConnectionManager
import com.sandeep.womenincloudrvce.util.RequestQueueSingleton
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EventActivity : AppCompatActivity() {
    lateinit var itemArray: ArrayList<Event>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var bindingProjectActivity: ActivityEventBinding
    lateinit var auth: FirebaseAuth
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProjectActivity = ActivityEventBinding.inflate(layoutInflater)
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
        val url = resources.getString(R.string.eventList)
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
                            val id :Int=jsonObject.getInt("event_id")
                            val company_name: String = jsonObject.getString("name")
                            val description: String =
                                "Description: " + jsonObject.getString("description")
                            val start_date: String =jsonObject.getString("start_date").substring(0,10)+" "+jsonObject.getString("start_date").substring(11,19)
                            val end_date: String =jsonObject.getString("end_date").substring(0,10)+" "+jsonObject.getString("end_date").substring(11,19)
                            val type: String = jsonObject.getString("type")
                            val location: String = "Venue: " + jsonObject.getString("location")
                            val poster_url: String = jsonObject.getString("poster")
                            itemArray.add(
                                Event(
                                    id,
                                    company_name,
                                    description,
                                    start_date,
                                    end_date,
                                    type,
                                    location,
                                    poster_url
                                )
                            )


                        }
                        bindingProjectActivity.vwRecyclerView.layoutManager = layoutManager
                        bindingProjectActivity.vwRecyclerView.adapter =
                            EventAdapter(this, itemArray)
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
            bindingProjectActivity.barProgress.stopRippleAnimation()
            bindingProjectActivity.txtError.visibility = View.VISIBLE
            ConnectionManager().createDialog(bindingProjectActivity.lytRel, this)
        }

    }

    fun ToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}