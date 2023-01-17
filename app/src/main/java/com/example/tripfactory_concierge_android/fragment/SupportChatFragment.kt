package com.example.tripfactory_concierge_android.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.activity.ChatActivity
import com.example.tripfactory_concierge_android.activity.MainActivity
import com.example.tripfactory_concierge_android.databinding.ActivityMainBinding
import com.example.tripfactory_concierge_android.databinding.FragmentStorageBinding
import com.example.tripfactory_concierge_android.databinding.FragmentSupportChatBinding


class SupportChatFragment : Fragment(), OnClickListener {

    private lateinit var bindingSupportChat: FragmentSupportChatBinding
    private lateinit var bindingMainActivity: ActivityMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingSupportChat = FragmentSupportChatBinding.inflate(inflater, container, false)
        bindingMainActivity = (activity as MainActivity).bindingMainActivity
        //Click listeners
        bindingSupportChat.ivBack.setOnClickListener(this)
        bindingSupportChat.ivHamburger.setOnClickListener(this)
        bindingSupportChat.cvChat.setOnClickListener(this)
        bindingSupportChat.cvEmail.setOnClickListener(this)
        return bindingSupportChat.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //Open drawer
            R.id.ivHamburger -> bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
            //Go back to home fragment
            R.id.ivBack -> {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(bindingMainActivity.lytFrame.id, HomeFragment(), "HomeFragment")
                    .commit()
                bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.home).isChecked = true
            }
            //Intent to chat activity
            R.id.cvChat -> {
                val chatIntent = Intent(activity, ChatActivity::class.java)
                activity?.startActivity(chatIntent)
            }
            //Intent to email service application
            R.id.cvEmail -> {
                //TODO
                val to = "tripfactory@gmail.com"
                val subject = "REQUEST HELP"
                val body =
                    "Please clearly mention your problem."
                val mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body)
                val emailIntent = Intent(Intent.ACTION_VIEW)
                emailIntent.data = Uri.parse(mailTo)
                (activity as MainActivity).startActivity(emailIntent)
            }
        }
    }

}