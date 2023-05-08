package com.example.sltchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.sltchat.R
import com.example.sltchat.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.msgSend.setOnClickListener{
            if(binding.messageText.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your message", Toast.LENGTH_SHORT).show()
            }else{

                sendMessage(binding.messageText.text.toString())
            }
        }




    }

    private fun sendMessage(msg: String) {

        val receiverId = intent.getStringExtra("userId")
        val senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber


        val chatId = senderId+receiverId

        // Current time and date
        val currentDate : String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime : String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["sender"] = senderId!!
        map["receiver"] = receiverId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        val  reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId)


            reference.child(reference.push().key!!).setValue(map).addOnCompleteListener{
                if(it.isSuccessful){
                    binding.messageText.text = null
                    Toast.makeText(this,"Message Sended",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show()

                }
            }
    }
}