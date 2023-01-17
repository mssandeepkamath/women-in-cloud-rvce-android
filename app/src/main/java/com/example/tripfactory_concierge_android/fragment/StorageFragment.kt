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
import com.example.tripfactory_concierge_android.activity.DocumentActivity
import com.example.tripfactory_concierge_android.activity.MainActivity
import com.example.tripfactory_concierge_android.activity.VoucherActivity
import com.example.tripfactory_concierge_android.databinding.ActivityMainBinding
import com.example.tripfactory_concierge_android.databinding.FragmentStorageBinding


class StorageFragment : Fragment(),OnClickListener{

    private lateinit var bindingStorageFragment: FragmentStorageBinding
    private lateinit var bindingMainActivity: ActivityMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingStorageFragment=FragmentStorageBinding.inflate(inflater,container,false)
        bindingMainActivity = (activity as MainActivity).bindingMainActivity
        bindingStorageFragment.ivBack.setOnClickListener(this)
        bindingStorageFragment.ivHamburger.setOnClickListener(this)
        bindingStorageFragment.cvDocument.setOnClickListener(this)
        bindingStorageFragment.cvVoucher.setOnClickListener(this)
        return bindingStorageFragment.root
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
            //Intent to document activity
            R.id.cvDocument -> {
                val documentIntent = Intent(activity, DocumentActivity::class.java)
                activity?.startActivity(documentIntent)
            }
            //Intent to voucher activity
            R.id.cvVoucher -> {
                val voucherIntent = Intent(activity, VoucherActivity::class.java)
                activity?.startActivity(voucherIntent)
            }
        }

    }

}