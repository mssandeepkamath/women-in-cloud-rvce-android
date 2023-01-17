package com.example.tripfactory_concierge_android.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.activity.MainActivity
import com.example.tripfactory_concierge_android.databinding.ActivityMainBinding
import com.example.tripfactory_concierge_android.databinding.FragmentStorageBinding
import com.example.tripfactory_concierge_android.databinding.FragmentSupportChatBinding


class SupportChatFragment : Fragment() {

    private lateinit var bindingSupportChat:FragmentSupportChatBinding
    private lateinit var bindingMainActivity: ActivityMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingSupportChat=FragmentSupportChatBinding.inflate(inflater,container,false)
        bindingMainActivity=(activity as MainActivity).bindingMainActivity
        bindingSupportChat.ivHamburger.setOnClickListener {
            bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
        }
        bindingSupportChat.ivBack.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(bindingMainActivity.lytFrame.id,HomeFragment(),"HomeFragment").commit()
            bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.home).isChecked=true
        }
        return bindingSupportChat.root
    }

}