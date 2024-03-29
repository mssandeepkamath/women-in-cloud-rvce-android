package com.sandeep.womenincloudrvce.ui.activities

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandeep.womenincloudrvce.R
import com.sandeep.womenincloudrvce.adapters.MemberListItemsAdapter
import com.sandeep.womenincloudrvce.firebase.FirestoreClass
import com.sandeep.womenincloudrvce.models.Board
import com.sandeep.womenincloudrvce.models.NotificationData
import com.sandeep.womenincloudrvce.models.PushNotification
import com.sandeep.womenincloudrvce.models.User
import com.sandeep.womenincloudrvce.repository.NotificationRepository
import com.sandeep.womenincloudrvce.utils.Constants
import com.sandeep.womenincloudrvce.viewmodel.NotificationViewModel
import com.sandeep.womenincloudrvce.viewmodel.NotificationViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.dialog_search_member.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MembersActivity : BaseActivity() {

    lateinit var mBoardDetails: Board
    lateinit var mAssignedMembersList: ArrayList<User>
    private var anyChangesMade: Boolean = false
    private lateinit var viewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }

        val notificationRepository = NotificationRepository()
        val viewModelProviderFactory = NotificationViewModelProviderFactory(notificationRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NotificationViewModel::class.java)

        setupActionBar()

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }

    fun setupMembersList(list: ArrayList<User>){
        mAssignedMembersList = list
        hideProgressDialog()
        rv_members_list.layoutManager = LinearLayoutManager(this)
        rv_members_list.setHasFixedSize(true)

        val adapter = MemberListItemsAdapter(this, list)
        rv_members_list.adapter = adapter
    }

    fun memberDetails(user: User){
        mBoardDetails.assignedTo.add(user.id!!)
        FirestoreClass().assignMemberToBoard(this, mBoardDetails, user)
    }

    fun memberAssignSuccess(user: User){
        hideProgressDialog()
        mAssignedMembersList.add(user)
        anyChangesMade = true
        setupMembersList(mAssignedMembersList)
        Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
        val notification = NotificationData(
            "Assigned to the Board ${mBoardDetails.name}",
            "You have been assigned to the new board by ${mAssignedMembersList[0].name}"
        )

        val pushNotification = PushNotification(notification, user.fcmToken!!)
        sendNotification(pushNotification)
    }

    fun sendNotification(notification: PushNotification){
        lifecycleScope.launch {
            viewModel.sendNotification(notification).join()
            withContext(Dispatchers.Main){
                if(viewModel.message != null){
                    Toast.makeText(this@MembersActivity, viewModel.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if(anyChangesMade){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_member -> {
                dialogSearchMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogSearchMember(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_member)
        dialog.tv_add.setOnClickListener {
            val email = dialog.et_email_search_member.text.toString()
            if(email.isNotEmpty()){
                dialog.dismiss()
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getMemberDetails(this, email)
            }else{
                Toast.makeText(this,"Please members enter email address", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.tv_cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_members_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }

        toolbar_members_activity.setNavigationOnClickListener { onBackPressed() }
    }

}