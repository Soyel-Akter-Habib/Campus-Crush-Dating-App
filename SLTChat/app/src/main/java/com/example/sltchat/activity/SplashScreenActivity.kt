package com.example.sltchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import com.example.sltchat.MainActivity
import com.example.sltchat.R
import com.example.sltchat.authen.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = FirebaseAuth.getInstance().currentUser
        val videoView : VideoView =findViewById(R.id.splash)
        videoView.setVideoPath("android.resource://" + packageName + "/" + R.raw.splash)
        videoView.start()
        Handler(Looper.getMainLooper()).postDelayed({
            if(user==null)
                startActivity(Intent(this,LoginActivity::class.java))
            else
                startActivity(Intent(this,MainActivity::class.java))
            finish()


        },5000)
    }
}