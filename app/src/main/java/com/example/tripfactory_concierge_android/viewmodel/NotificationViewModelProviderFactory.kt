package com.example.tripfactory_concierge_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripfactory_concierge_android.repository.NotificationRepository

class NotificationViewModelProviderFactory(
    private val notificationRepository: NotificationRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationViewModel(notificationRepository) as T
    }

}
