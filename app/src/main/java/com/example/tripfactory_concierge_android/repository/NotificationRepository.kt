package com.example.tripfactory_concierge_android.repository

import com.example.tripfactory_concierge_android.api.RetrofitInstance
import com.example.tripfactory_concierge_android.models.PushNotification

class NotificationRepository {
    suspend fun postNotification(notification: PushNotification) = RetrofitInstance.api.postNotification(notification)
}