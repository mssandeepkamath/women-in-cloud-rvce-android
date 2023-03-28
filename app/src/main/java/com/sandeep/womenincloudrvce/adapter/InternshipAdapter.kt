package com.sandeep.womenincloudrvce.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandeep.womenincloudrvce.activity.DescriptionActivity
import com.sandeep.womenincloudrvce.databinding.InternshipItemBinding
import com.sandeep.womenincloudrvce.entity.Internship

import com.google.firebase.auth.FirebaseAuth

class InternshipAdapter(val context: Context, val itemArrayList: ArrayList<Internship>) :
    RecyclerView.Adapter<InternshipAdapter.ViewHolderInternship>() {

    lateinit var auth: FirebaseAuth

    inner class ViewHolderInternship(val binding: InternshipItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInternship {
        val binding =
            InternshipItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderInternship(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderInternship, position: Int) {

        with(holder) {
            with(itemArrayList[position]) {
                binding.tvName.text = this.company_name
                binding.tvOpen.text = this.opening.toString()
                binding.tvDescription.text = this.role_description
                binding.tvMode.text = "Mode: " + this.mode + "\t\t\t\tType: " + this.type
                binding.tvStart.text = this.start_date
                binding.tvEnd.text = this.end_date
                binding.btnProject.setOnClickListener {
                    val intent = Intent(context, DescriptionActivity::class.java)
                    intent.putExtra("INTERNSHIP", this)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemArrayList.size
    }
}