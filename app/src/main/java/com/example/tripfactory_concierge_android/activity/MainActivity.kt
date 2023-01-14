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
        supportFragmentManager.beginTransaction().replace(bindingMainActivity.lytFrame.id,
            HomeFragment(),
            "homeFragment").addToBackStack(null).commit()
        bindingMainActivity.vwBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(bindingMainActivity.lytFrame.id,
                        HomeFragment()).addToBackStack(null).commit()
                }
                R.id.chat -> {
                    supportFragmentManager.beginTransaction().replace(bindingMainActivity.lytFrame.id,
                        SupportChatFragment()).addToBackStack(null).commit()
                }
                R.id.storage -> {
                    supportFragmentManager.beginTransaction().replace(bindingMainActivity.lytFrame.id,
                        StorageFragment()).addToBackStack(null).commit()
                }
            }
            return@setOnItemSelectedListener true
        }
    }


}