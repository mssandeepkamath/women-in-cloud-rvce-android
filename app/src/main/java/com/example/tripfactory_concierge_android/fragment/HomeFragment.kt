package com.example.tripfactory_concierge_android.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.activity.MainActivity
import com.example.tripfactory_concierge_android.activity.PlaceActivity
import com.example.tripfactory_concierge_android.activity.RestRoomActivity
import com.example.tripfactory_concierge_android.activity.RestaurantActivity
import com.example.tripfactory_concierge_android.databinding.ActivityMainBinding
import com.example.tripfactory_concierge_android.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(),OnClickListener {


    private lateinit var bindingHomeFragment: FragmentHomeBinding
    private lateinit var bindingMainActivity: ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //viewBinding fragment_home
        bindingHomeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        //viewBinding activity_main
        bindingMainActivity=(activity as MainActivity).bindingMainActivity
        //Close drawer on navigationDrawerHeader click
        bindingMainActivity.vwNavigation.getHeaderView(0).setOnClickListener {
            bindingMainActivity.lytDrawer.closeDrawers()
        }
        //Display date
        val formatTime=SimpleDateFormat("hh:mm aa", Locale.US)
        val time=formatTime.format(Date())
        bindingHomeFragment.tvDate.text="${time.substring(0,5)}\n ${time.substring(6)}"
        //on click declarations
        bindingHomeFragment.ivHamburger.setOnClickListener(this)
        bindingHomeFragment.lytResturants.setOnClickListener(this)
        bindingHomeFragment.lytPlaces.setOnClickListener(this)
        bindingHomeFragment.lytRestRooms.setOnClickListener(this)
        return bindingHomeFragment.root
    }

    override fun onClick(v: View?) {
       when(v?.id)
       {
           //Open drawer on hamburgerIcon click
           R.id.ivHamburger->  bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
           R.id.lytResturants->{
               intentProvider(RestaurantActivity())
           }
           R.id.lytPlaces->{
               intentProvider(PlaceActivity())
           }
           R.id.lytRestRooms->{
               intentProvider(RestRoomActivity())
           }
       }
    }
    private fun intentProvider(activityName:Activity)
    {
        //Intent to respective activities
        val intent= Intent(activity as MainActivity,activityName::class.java)
        (activity as MainActivity).startActivity(intent)
    }


}