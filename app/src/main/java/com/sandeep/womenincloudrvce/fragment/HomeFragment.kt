package com.sandeep.womenincloudrvce.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.sandeep.womenincloudrvce.R
import com.sandeep.womenincloudrvce.activity.*
import com.sandeep.womenincloudrvce.databinding.ActivityMainBinding
import com.sandeep.womenincloudrvce.databinding.FragmentHomeBinding
import com.magicgoop.tagsphere.OnTagTapListener
import com.magicgoop.tagsphere.item.TagItem
import com.magicgoop.tagsphere.item.TextTagItem
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.*


class HomeFragment : Fragment(), OnClickListener {


    private lateinit var bindingHomeFragment: FragmentHomeBinding
    private lateinit var bindingMainActivity: ActivityMainBinding
    private var list = mutableListOf<CarouselItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //viewBinding fragment_home
        bindingHomeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        //viewBinding activity_main
        bindingMainActivity = (activity as MainActivity).bindingMainActivity

        //Close drawer on navigationDrawerHeader click
        bindingMainActivity.vwNavigation.getHeaderView(0).setOnClickListener {
            bindingMainActivity.lytDrawer.closeDrawers()
        }
        bindingHomeFragment.ivHamburger.setOnClickListener(this)
        bindingHomeFragment.lytProjects.setOnClickListener(this)
        bindingHomeFragment.lytInternship.setOnClickListener(this)
        bindingHomeFragment.lytEvent.setOnClickListener(this)
        bindingHomeFragment.lytmore.setOnClickListener(this)
        bindingHomeFragment.lytstudents.setOnClickListener(this)
        bindingHomeFragment.lytarchives.setOnClickListener(this)

        bindingHomeFragment.carousel.registerLifecycle(viewLifecycleOwner)
        list.add(CarouselItem(imageDrawable = R.drawable.p2580883_51954099567_o))
        list.add(CarouselItem(imageDrawable = R.drawable.p2580896_51955154038_o))
        list.add(CarouselItem(imageDrawable = R.drawable.p2580961_51955154873_o))
        list.add(CarouselItem(imageDrawable = R.drawable.p2580965_51955389649_o))
        list.add(CarouselItem(imageDrawable = R.drawable.p2580986_51955162953_o))
        list.add(CarouselItem(imageDrawable = R.drawable.p2580997_51955688650_o))




        bindingHomeFragment.carousel.setData(list);

        return bindingHomeFragment.root
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            //Open drawer on hamburgerIcon click
            R.id.ivHamburger -> bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
            R.id.lytProjects -> {
                intentProvider(ProjectActivity())
            }
            R.id.lytInternship -> {
                intentProvider(InternshipActivity())
            }
            R.id.lytEvent-> {
                intentProvider(EventActivity())
            }
            R.id.lytmore-> {
                bindingMainActivity.lytDrawer.openDrawer(GravityCompat.START)
            }
            R.id.lytarchives-> {
                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(bindingMainActivity.lytFrame.id, SupportChatFragment(), tag).commit()
                bindingMainActivity.vwBottomNavigation.menu.findItem(R.id.workspace).isChecked=true
            }
            R.id.lytstudents-> {
                intentProvider(VoucherActivity())
            }
        }
    }

    private fun intentProvider(activityName: Activity) {
        //Intent to respective activities
        val intent = Intent(activity as MainActivity, activityName::class.java)
        (activity as MainActivity).startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTagView()
    }

    private fun initTagView() {
        bindingHomeFragment.tagSphereView.setTextPaint(
            TextPaint().apply {
                isAntiAlias = true
                textSize = resources.getDimension(com.intuit.sdp.R.dimen._15sdp)
                color = resources.getColor(R.color.light_blue)
            }
        )
        val member = arrayListOf<String>("Chaitra Vedullapalli","Priya D","G S Mamatha","K N Subramanya","B M Sagar","Anala M R","Karen Fassio","Carrie Francey","Gretchen O'Hara","Smitha R","Shanmukha Nagaraj","Uma B V","Gina Fratarcangeli","Amy Protexter","Amy Protexter","Amy Protexter","Geetha Gandhi","Feranda Green","Syren Jordan","Rajiv Kapoor")
        member.map {
            TextTagItem(text = it)
        }.toList().let {
            bindingHomeFragment.tagSphereView.addTagList(it)
        }
        bindingHomeFragment.tagSphereView.setRadius(3f)
        bindingHomeFragment.tagSphereView.setOnTagTapListener(object : OnTagTapListener {
            override fun onTap(tagItem: TagItem) {
                Toast.makeText(
                    requireContext(),
                    "${(tagItem as TextTagItem).text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}