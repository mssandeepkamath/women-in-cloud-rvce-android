package com.sandeep.womenincloudrvce.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.sandeep.womenincloudrvce.R
import com.sandeep.womenincloudrvce.activity.LoginActivity
import com.sandeep.womenincloudrvce.activity.MainActivity
import com.sandeep.womenincloudrvce.databinding.ActivityMainBinding
import com.sandeep.womenincloudrvce.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment(), OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var bindingAccountFragment: FragmentAccountBinding
    private lateinit var bindingMainActivity: ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        bindingAccountFragment = FragmentAccountBinding.inflate(layoutInflater, container, false)
        bindingMainActivity = (activity as MainActivity).bindingMainActivity
        auth = FirebaseAuth.getInstance()
        val current_user = auth.currentUser
        bindingAccountFragment.txtEmail.text = current_user?.email
        bindingAccountFragment.txtName.text = current_user?.email?.substringBefore(".").toString().uppercase()
//        bindingAccountFragment.lytShimmerAccount.startShimmer()

//        Glide.with(activity as Context).load(current_user?.photoUrl)
//            .placeholder(R.drawable.placeholder).listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean,
//                ): Boolean {
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean,
//                ): Boolean {
//                    bindingAccountFragment.lytShimmerAccount.hideShimmer()
//                    return false
//                }
//            }).into(bindingAccountFragment.imgProfile)

        bindingAccountFragment.lytLogOut.setOnClickListener(this)
        bindingAccountFragment.txtAboutUs.setOnClickListener(this)
        bindingAccountFragment.lytReport.setOnClickListener(this)
        bindingAccountFragment.ivBack.setOnClickListener(this)
        bindingAccountFragment.ivHamburger.setOnClickListener(this)
        bindingAccountFragment.lytContactUs.setOnClickListener(this)
        bindingAccountFragment.lytAboutUs.setOnClickListener(this)

        return bindingAccountFragment.root
    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.lytAboutUs -> {
                var intent = Intent(Intent.ACTION_VIEW)
                intent.data=Uri.parse("https://womenincloud.com/about-us/")
                (activity as MainActivity).startActivity(intent)
            }
            R.id.lytReport -> {
                val to = "msandeepcip@gmail.com"
                val subject = "I FOUND A BUG IN WIC-RVCE ANDROID APP!"
                val body =
                    "Please clearly mention page name, bug description, and other useful details here."
                val mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body)
                val emailIntent = Intent(Intent.ACTION_VIEW)
                emailIntent.data = Uri.parse(mailTo)
                startActivity(emailIntent)
            }
            R.id.lytContactUs -> {

                val to = "msandeepcip@gmail.com"
                val subject = "NEED SUPPORT"
                val body =
                    "Please clearly mention your concern."
                val mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body)
                val emailIntent = Intent(Intent.ACTION_VIEW)
                emailIntent.data = Uri.parse(mailTo)
                (activity as MainActivity).startActivity(emailIntent)

            }
            R.id.lytLogOut -> {
                auth.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                (activity as MainActivity).finish()
            }
            R.id.ivHamburger -> bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
            //Go back to home fragment
            R.id.ivBack -> {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(bindingMainActivity.lytFrame.id, HomeFragment(), "HomeFragment")
                    .commit()
                bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.home).isChecked = true
            }
        }
    }


}