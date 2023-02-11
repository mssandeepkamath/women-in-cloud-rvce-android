package com.example.tripfactory_concierge_android.adapter


import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tripfactory_concierge_android.databinding.InternshipItemBinding
import com.example.tripfactory_concierge_android.entity.Internship

import com.google.firebase.auth.FirebaseAuth

class InternshipAdapter(val context: Context, val itemArrayList: ArrayList<Internship>) :
    RecyclerView.Adapter<InternshipAdapter.ViewHolderInternship>() {

    lateinit var auth: FirebaseAuth

     inner class ViewHolderInternship(val binding: InternshipItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInternship {
        val binding = InternshipItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderInternship(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderInternship, position: Int) {

        with(holder) {
            with(itemArrayList[position]) {
                binding.tvName.text = this.company_name
                binding.tvOpen.text = this.opening.toString()
                binding.tvDescription.text = this.role_description
                binding.tvMode.text="Mode: "+ this.mode +"\t\t\t\tType: " + this.type
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