package com.example.tripfactory_concierge_android.adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.databinding.ProjectItemBinding
import com.example.tripfactory_concierge_android.entity.Project
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
                binding.btnProject.setOnClickListener {
                    Toast.makeText(context, "Hi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemArrayList.size
    }
}