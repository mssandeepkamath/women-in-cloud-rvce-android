package com.sandeep.womenincloudrvce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sandeep.womenincloudrvce.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var bindingChatActivity: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingChatActivity=ActivityChatBinding.inflate(layoutInflater)
        setContentView(bindingChatActivity.root)
    }
}