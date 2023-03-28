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
import com.google.firebase.auth.FirebaseAuth
import com.sandeep.womenincloudrvce.R
import com.sandeep.womenincloudrvce.adapter.ProjectAdapter
import com.sandeep.womenincloudrvce.adapter.StaffAdapter
import com.sandeep.womenincloudrvce.databinding.ActivityPlaceBinding
import com.sandeep.womenincloudrvce.databinding.ActivityProjectBinding
import com.sandeep.womenincloudrvce.entity.Project
import com.sandeep.womenincloudrvce.entity.Staff
import com.sandeep.womenincloudrvce.util.ConnectionManager
import com.sandeep.womenincloudrvce.util.RequestQueueSingleton
import kotlinx.android.synthetic.main.item_staff.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PlaceActivity : AppCompatActivity() {

    private lateinit var bindingPlaceActivity: ActivityPlaceBinding
    lateinit var itemArray: ArrayList<Staff>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var auth: FirebaseAuth
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPlaceActivity = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(bindingPlaceActivity.root)
        bindingPlaceActivity.ivBack.setOnClickListener {
            finish()
        }
        layoutManager = LinearLayoutManager(this)
        bindingPlaceActivity.lytRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green),
            ContextCompat.getColor(this, R.color.red)
        );
        bindingPlaceActivity.barProgress.startRippleAnimation()
        bindingPlaceActivity.lytProgress.visibility = View.VISIBLE
        bindingPlaceActivity.txtError.visibility = View.GONE
        auth = FirebaseAuth.getInstance()

        itemArray = ArrayList()
        volleyGetRequest()
        bindingPlaceActivity.lytRefresh.setOnRefreshListener(object :
            SwipeRefreshLayout.OnRefreshListener {

            override fun onRefresh() {
                bindingPlaceActivity.vwRecyclerView.adapter?.notifyDataSetChanged()
//                bindingProjectActivity.lytProgress.visibility = View.VISIBLE
//                bindingProjectActivity.barProgress.visibility = View.VISIBLE
//                bindingProjectActivity.txtError.visibility = View.GONE
                volleyGetRequest()
            }
        })

    }

    fun volleyGetRequest() {
        val url = resources.getString(R.string.staffList)
        if (ConnectionManager().checkConnectivity(this)) {
            val jsonObjectRequest = object : JsonArrayRequest(
                Method.GET,
                url,
                null,
                { response ->
                    try {
                        val jsonArray: JSONArray = response
                        itemArray.clear()
                        bindingPlaceActivity.lytProgress.visibility = View.GONE
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val first_name: String = jsonObject.getString("first_name")
                            val last_name: String = jsonObject.getString("last_name")
                            val department: String = jsonObject.getString("department")
                            val email: String = jsonObject.getString("email")

                            itemArray.add(
                                Staff(department,first_name,last_name,email
                                )
                            )

                        }
                        bindingPlaceActivity.vwRecyclerView.layoutManager = layoutManager
                        bindingPlaceActivity.vwRecyclerView.adapter = StaffAdapter(this, itemArray)
                        bindingPlaceActivity.lytRefresh.isRefreshing = false
                    } catch (e: JSONException) {
                        bindingPlaceActivity.lytProgress.visibility = View.VISIBLE
                        bindingPlaceActivity.barProgress.visibility = View.GONE
                        bindingPlaceActivity.barProgress.stopRippleAnimation()
                        bindingPlaceActivity.txtError.visibility = View.VISIBLE
                        ToastMessage("Some unexpected error occurred.")
                    }

                },
                { error ->
                    bindingPlaceActivity.barProgress.visibility = View.GONE
                    bindingPlaceActivity.barProgress.stopRippleAnimation()
                    bindingPlaceActivity.txtError.visibility = View.VISIBLE
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
            bindingPlaceActivity.barProgress.visibility = View.GONE
            bindingPlaceActivity.barProgress.stopRippleAnimation()
            bindingPlaceActivity.txtError.visibility = View.VISIBLE
            ConnectionManager().createDialog(bindingPlaceActivity.vwRecyclerView, this)
        }
    }

    fun ToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}