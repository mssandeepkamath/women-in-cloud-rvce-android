package com.example.tripfactory_concierge_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tripfactory_concierge_android.R
import com.example.tripfactory_concierge_android.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var bindingChatActivity: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingChatActivity=ActivityChatBinding.inflate(layoutInflater)
        setContentView(bindingChatActivity.root)
    }
}