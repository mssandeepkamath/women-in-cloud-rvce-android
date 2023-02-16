package com.example.tripfactory_concierge_android.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripfactory_concierge_android.activity.DescriptionActivity
import com.example.tripfactory_concierge_android.databinding.EventItemBinding
import com.example.tripfactory_concierge_android.entity.Event
import com.google.firebase.auth.FirebaseAuth

class EventAdapter(val context: Context, val itemArrayList: ArrayList<Event>) :
    RecyclerView.Adapter<EventAdapter.ViewHolderEvent>() {

    lateinit var auth: FirebaseAuth

    inner class ViewHolderEvent(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EventAdapter.ViewHolderEvent {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderEvent(binding)
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolderEvent, position: Int) {

        with(holder) {
            with(itemArrayList[position]) {
                binding.tvName.text = this.name
                binding.tvDescription.text = this.location
                binding.tvMode.text = this.description
                binding.tvStart.text = this.start_date
                binding.tvEnd.text = this.end_date
                binding.tvOpen.text=this.type
                binding.btnProject.setOnClickListener {
                    val intent = Intent(context, DescriptionActivity::class.java)
                    intent.putExtra("EVENT", this)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemArrayList.size
    }
}