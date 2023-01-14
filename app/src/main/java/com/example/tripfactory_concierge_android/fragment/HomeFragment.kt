package com.example.tripfactory_concierge_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.tripfactory_concierge_android.activity.MainActivity
import com.example.tripfactory_concierge_android.databinding.ActivityMainBinding
import com.example.tripfactory_concierge_android.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {


    private lateinit var bindingHomeFragment: FragmentHomeBinding
    private lateinit var bindingMainActivity: ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //viewBinding fragment_home
        bindingHomeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        //viewBinding activity_main
        bindingMainActivity=(activity as MainActivity).binding
        //Open drawer on hamburgerIcon click
        bindingHomeFragment.ivHamburger.setOnClickListener {
            bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
        }
        //Close drawer on navigationDrawerHeader click
        bindingMainActivity.vwNavigation.getHeaderView(0).setOnClickListener {
            bindingMainActivity.lytDrawer.closeDrawers()
        }
        //return rootView

        val formatTime=SimpleDateFormat("hh:mm aa", Locale.US)
        val time=formatTime.format(Date())
        bindingHomeFragment.tvDate.text="${time.substring(0,5)}\n${time.substring(6)}"
        return bindingHomeFragment.root
    }


}