package com.example.tripfactory_concierge_android.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.example.tripfactory_concierge_android.adapters.BoardItemsAdapter
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.firebase.FirestoreClass
import com.example.tripfactory_concierge_android.models.Board
import com.example.tripfactory_concierge_android.models.User
import com.example.tripfactory_concierge_android.utils.Constants
import com.example.tripfactory_concierge_android.utils.SwipeToDeleteCallback
import com.example.tripfactory_concierge_android.utils.SwipeToEditCallback
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_trello.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val MY_PROFILE_REQUEST_CODE = 11
        const val CREATE_BOARD_REQUEST_CODE = 12
        const val EDIT_BOARD_REQUEST_CODE = 17
    }

    private var mUserName: String = ""
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_trello)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        nav_view.setNavigationItemSelectedListener(this)

        mSharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val tokenUpdated = mSharedPreferences.getBoolean(Constants.FCM_TOKEN_UPDATED, false)

        if(tokenUpdated){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().loadUserData(this, true)
        }else{

            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result != null && !TextUtils.isEmpty(task.result)) {
                            val token: String = task.result!!
                            updateFCMToken(token)
                        }
                    }
                }

        }

        FirestoreClass().loadUserData(this, true)

        fab_create_board.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_main_activty)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = resources.getString(R.string.name_of_action_bar)
        }
        toolbar_main_activty.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activty.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>){
        hideProgressDialog()
        if(boardsList.size > 0){
            rv_boards_list.visibility = View.VISIBLE
            tv_no_boards_available.visibility = View.GONE

            rv_boards_list.layoutManager = LinearLayoutManager(this)
            rv_boards_list.setHasFixedSize(true)
            val adapter = BoardItemsAdapter(this, boardsList)
            rv_boards_list.adapter = adapter
            adapter.setOnClickListener(object: BoardItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }

            })

            val editSwipeHandler = object: SwipeToEditCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = rv_boards_list.adapter as BoardItemsAdapter
                    adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, EDIT_BOARD_REQUEST_CODE)
                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(rv_boards_list)

            val deleteSwipeHandler = object: SwipeToDeleteCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = rv_boards_list.adapter as BoardItemsAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(rv_boards_list)
        }else{
            rv_boards_list.visibility = View.GONE
            tv_no_boards_available.visibility = View.VISIBLE
        }
    }

    private fun toggleDrawer(){
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == MY_PROFILE_REQUEST_CODE){
                FirestoreClass().loadUserData(this)
            }else if(requestCode == CREATE_BOARD_REQUEST_CODE || requestCode == EDIT_BOARD_REQUEST_CODE){
                FirestoreClass().getBoardsList(this)
            }
        }else{
            Log.e("Cancelled","Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User?, readBoardsList: Boolean) {
        hideProgressDialog()
        if(user != null){
            if(user.name != null && user.name != ""){
                mUserName = user.name
            }

            Glide
                .with(this)
                .load(user!!.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(nav_user_image)

            tv_username.text = user.name
            if(readBoardsList){
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getBoardsList(this)
            }
        }
    }

    fun tokenUpdateSuccess(){
        hideProgressDialog()
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(Constants.FCM_TOKEN_UPDATED, true)
        editor.apply()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().loadUserData(this, true)
    }

    private fun updateFCMToken(token: String){
        val userHashMap = HashMap<String,Any>()
        userHashMap[Constants.FCM_TOKEN] = token
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().updateUserProfileData(this, userHashMap)
    }
}