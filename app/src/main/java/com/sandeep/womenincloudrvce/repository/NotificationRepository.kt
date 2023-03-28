package com.sandeep.womenincloudrvce.repository

import com.sandeep.womenincloudrvce.api.RetrofitInstance
import com.sandeep.womenincloudrvce.models.PushNotification

class NotificationRepository {
    suspend fun postNotification(notification: PushNotification) = RetrofitInstance.api.postNotification(notification)
}