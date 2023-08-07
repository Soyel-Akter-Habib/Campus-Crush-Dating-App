package com.example.sltchat.notification.api

import android.app.Notification
import com.example.sltchat.notification.model.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {


    @Headers("Content-Type:application/json","Authorization:key=AAAAcLug9rQ:APA91bFDCsyC3kbOhgL5ow0hQ2W5A-YAsQS6-sWDk0Ubq4YYhRS8Iqlzjphl8TIMkyTDeXrtK6y3jbzRY6gcmPVggF_itOmJE1mlwxUorrzW5atZuBw2XiLO_lxoUZxIJy8eVl1IMrOn")

    @POST("fcm/send")

    fun sendNotification(@Body notification: PushNotification)
    : Call<PushNotification>

}