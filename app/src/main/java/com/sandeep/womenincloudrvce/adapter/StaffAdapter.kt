package com.sandeep.womenincloudrvce.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sandeep.womenincloudrvce.activity.DescriptionActivity
import com.sandeep.womenincloudrvce.databinding.ItemStaffBinding
import com.sandeep.womenincloudrvce.entity.Staff


class StaffAdapter(val context: Context, val itemArrayList: ArrayList<Staff>) :
    RecyclerView.Adapter<StaffAdapter.ViewHolderStaff>() {

    lateinit var auth: FirebaseAuth

    inner class ViewHolderStaff(val binding:ItemStaffBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ):ViewHolderStaff {
        val binding = ItemStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderStaff(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderStaff, position: Int) {
        with(holder) {
            with(itemArrayList[position]) {
                binding.name.text = "Name: " + this.first_name +" " +this.last_name
                binding.dept.text = "Department: " + this.department
                binding.email.setOnClickListener {
                    val to = this.email
                    val mailTo = "mailto:$to"
                    val emailIntent = Intent(Intent.ACTION_VIEW)
                    emailIntent.data = Uri.parse(mailTo)
                    context.startActivity(emailIntent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemArrayList.size
    }
}