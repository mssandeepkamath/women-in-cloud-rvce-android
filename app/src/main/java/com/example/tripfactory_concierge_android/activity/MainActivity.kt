package com.example.tripfactory_concierge_android.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.databinding.ActivityMainBinding
import com.example.tripfactory_concierge_android.fragment.HomeFragment
import com.example.tripfactory_concierge_android.fragment.StorageFragment
import com.example.tripfactory_concierge_android.fragment.SupportChatFragment

class MainActivity : AppCompatActivity() {

    lateinit var bindingMainActivity: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMainActivity.root)
        //Load home fragment on activity create
        replaceFragment(bindingMainActivity.lytFrame.id,HomeFragment(),"HomeFragment")

        bindingMainActivity.vwBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                  replaceFragment(bindingMainActivity.lytFrame.id,HomeFragment(),"HomeFragment")
                }
                R.id.chat -> {
                    replaceFragment(bindingMainActivity.lytFrame.id,SupportChatFragment(),"SupportChatFragment")
                }
                R.id.storage -> {
                    replaceFragment(bindingMainActivity.lytFrame.id,StorageFragment(),"StorageFragment")
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun replaceFragment(frameLayoutId : Int, fragment:Fragment,tag:String)
    {
        supportFragmentManager.beginTransaction().replace(frameLayoutId,fragment,tag).commit()
    }

    override fun onBackPressed() {
        val fragment=supportFragmentManager.findFragmentById(bindingMainActivity.lytFrame.id)
        when(fragment)
        {
            !is HomeFragment->
            {
                replaceFragment(bindingMainActivity.lytFrame.id,HomeFragment(),"HomeFragment")
                bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.home).isChecked = true
            }
            else->super.onBackPressed()
        }
    }








}