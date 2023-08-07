package com.example.sltchat.notification.model

import com.example.sltchat.notification.model.NotificationData

data class PushNotification(
    val data: NotificationData,
    val to : String? ="",


    )
