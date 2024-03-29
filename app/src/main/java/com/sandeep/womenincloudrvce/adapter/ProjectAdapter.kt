package com.sandeep.womenincloudrvce.adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandeep.womenincloudrvce.activity.DescriptionActivity
import com.sandeep.womenincloudrvce.databinding.ProjectItemBinding
import com.sandeep.womenincloudrvce.entity.Project
import com.google.firebase.auth.FirebaseAuth

class ProjectAdapter(val context: Context, val itemArrayList: ArrayList<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ViewHolderProject>() {

    lateinit var auth: FirebaseAuth
    lateinit var dialog: ProgressDialog

    inner class ViewHolderProject(val binding: ProjectItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProject {
        val binding = ProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderProject(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderProject, position: Int) {


        with(holder) {
            with(itemArrayList[position]) {
                binding.tvName.text = this.company_name
                binding.tvOpen.text = this.opening.toString()
                binding.tvDescription.text = this.description
                binding.tvStart.text = this.start_date
                binding.tvEnd.text = this.end_date
                binding.btnProject.setOnClickListener {
                    val intent = Intent(context, DescriptionActivity::class.java)
                    intent.putExtra("PROJECT", this)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemArrayList.size
    }
}