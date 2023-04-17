package com.sandeep.womenincloudrvce.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sandeep.womenincloudrvce.R
import com.sandeep.womenincloudrvce.databinding.ActivityMainBinding
import com.sandeep.womenincloudrvce.fragment.HomeFragment
import com.sandeep.womenincloudrvce.fragment.AccountFragment
import com.sandeep.womenincloudrvce.fragment.SupportChatFragment

class MainActivity : AppCompatActivity() {

    lateinit var bindingMainActivity: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMainActivity.root)
        //Load home fragment on activity create
        replaceFragment(bindingMainActivity.lytFrame.id, HomeFragment(), "HomeFragment")
        //check the first item of bottom navigation on start
        bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.home).isChecked = true

        //bottom navigation OnClick function
        bindingMainActivity.vwBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(bindingMainActivity.lytFrame.id, HomeFragment(), "HomeFragment")
                }
                R.id.workspace -> {
                    replaceFragment(
                        bindingMainActivity.lytFrame.id,
                        SupportChatFragment(),
                        "SupportChatFragment"
                    )
                }
                R.id.account -> {
                    replaceFragment(
                        bindingMainActivity.lytFrame.id,
                        AccountFragment(),
                        "AccountFragment"
                    )
                }
            }
            return@setOnItemSelectedListener true
        }

        //navigation drawer onclick function
        bindingMainActivity.vwNavigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dlProject -> {
                    intentProvider(ProjectActivity())
                }
                R.id.dlInternship -> {
                    intentProvider(InternshipActivity())
                }
                R.id.dlEvent -> {
                    intentProvider(EventActivity())
                }
                R.id.workspace -> {
                    intentProvider(VoucherActivity())
                }
                R.id.about_us -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data=Uri.parse("https://womenincloud.com/about-us/")
                    startActivity(intent)
                }
                R.id.rate_us -> {
                    val playStoreIntent = Intent(Intent.ACTION_VIEW)
                    playStoreIntent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.sandeep.womenincloudrvce")
                    bindingMainActivity.lytDrawer.closeDrawers()
                    startActivity(playStoreIntent)
                }
                R.id.privacy -> {
                    val intent=Intent(Intent.ACTION_VIEW)
                    intent.data=Uri.parse("https://www.privacypolicygenerator.info/live.php?token=mMuT3Ssb0u3P0S83LSfkUkOMy3YwpeSm")
                    bindingMainActivity.lytDrawer.closeDrawers()
                    startActivity(intent)
                }

                R.id.staff -> {
                    intentProvider(PlaceActivity())
                }

                R.id.workloud -> {

                    replaceFragment(
                        bindingMainActivity.lytFrame.id,
                        SupportChatFragment(),
                        "SupportChatFragment"
                    )
                    bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.workspace).isChecked=true
                    bindingMainActivity.lytDrawer.closeDrawers()
                }
                R.id.account -> {
                    replaceFragment(
                        bindingMainActivity.lytFrame.id,
                        AccountFragment(),
                        "AccountFragment"
                    )
                    bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.account).isChecked=true
                    bindingMainActivity.lytDrawer.closeDrawers()
                }

                R.id.bug -> {
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
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun replaceFragment(frameLayoutId: Int, fragment: Fragment, tag: String) {
        //replacing the fragment
        supportFragmentManager.beginTransaction().replace(frameLayoutId, fragment, tag).commit()
    }


    override fun onBackPressed() {
        //get the fragment currently attached to frame layout
        val fragment = supportFragmentManager.findFragmentById(bindingMainActivity.lytFrame.id)
        when (fragment) {
            //if fragment attached is not home fragment, then on back pressed come back to home fragment
            !is HomeFragment -> {
                replaceFragment(bindingMainActivity.lytFrame.id, HomeFragment(), "HomeFragment")
                bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.home).isChecked = true
            }
            //Else follow the default behaviour of activity stack pop
            else -> super.onBackPressed()
        }
    }

    private fun intentProvider(activityName: Activity) {
        //Intent to respective activities
        val intent = Intent(this, activityName::class.java)
        bindingMainActivity.lytDrawer.closeDrawers()
        startActivity(intent)
    }

}