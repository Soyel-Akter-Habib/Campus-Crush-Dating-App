package com.example.sltchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.sltchat.R
import com.example.sltchat.adapter.MessageAdapter
import com.example.sltchat.databinding.ActivityChatBinding
import com.example.sltchat.model.MessageModel
import com.example.sltchat.model.UserModel
import com.example.sltchat.notification.api.ApiUtilities
import com.example.sltchat.notification.model.NotificationData
import com.example.sltchat.notification.model.PushNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        getData(intent.getStringExtra("chat_id"))
            verifyChatId()


        binding.msgSend.setOnClickListener{
            if(binding.messageText.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your message", Toast.LENGTH_SHORT).show()
            }else{

                storeData(binding.messageText.text.toString())
            }
        }




    }
    private var senderId:String? =null
    private var chatId:String? =null
    private var receiverId:String? =null
    private var reverseChatId:String? =null

    private fun verifyChatId() {
         receiverId  = intent.getStringExtra("userId")
         senderId  = FirebaseAuth.getInstance().currentUser!!.phoneNumber
         chatId  = senderId + receiverId
         reverseChatId = receiverId+senderId

        // Current time and date


        val  reference = FirebaseDatabase.getInstance().getReference("chats")

        reference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChild(chatId!!)){
                    getData(chatId)
                }else if(snapshot.hasChild(reverseChatId!!)){
                    chatId = reverseChatId
                    getData(chatId)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getData(chatId: String?) {
            FirebaseDatabase.getInstance().getReference("chats")
                .child(chatId!!).addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val list = arrayListOf<MessageModel>()
                        for ( sh in snapshot.children){
                            list.add(sh.getValue(MessageModel::class.java)!!)
                        }

                        binding.messageRecycler.adapter = MessageAdapter(this@ChatActivity,list)

                    }

                    override fun onCancelled(error: DatabaseError) {

                        Toast.makeText(this@ChatActivity,error.message,Toast.LENGTH_SHORT).show()
                    }

                })
    }

//    private fun sendMessage(msg: String) {
//
//
////        val receiverId  = intent.getStringExtra("userId")
////        val senderId  = FirebaseAuth.getInstance().currentUser!!.phoneNumber
////
////
////        val chatId  = senderId + receiverId
////        val reverseChatId = receiverId+senderId
//
//        // Current time and date
//
//
//        val  reference = FirebaseDatabase.getInstance().getReference("chats")
//
//        reference.addListenerForSingleValueEvent(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if(snapshot.hasChild(reverseChatId)){
//                    storeData(reverseChatId,msg, senderId!!, receiverId!!)
//                }else{
//                    storeData(chatId,msg, senderId!!,receiverId!!)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//                Toast.makeText(this@ChatActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
//            }
//        })
//
//
//
//    }

    private fun storeData(msg:String) {
        val currentDate : String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime : String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["sender"] = senderId!!
        map["receiver"] = receiverId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate
        val  reference = FirebaseDatabase.getInstance().getReference("chats").child((chatId!!))

        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener{
            if(it.isSuccessful){
                binding.messageText.text = null


                sendNotification(msg)

                Toast.makeText(this,"Message Sended",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun sendNotification(msg: String) {

        FirebaseDatabase.getInstance().getReference("users")
            .child(receiverId!!).addListenerForSingleValueEvent(
                object  : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){
                            val data = snapshot.getValue(UserModel::class.java)

                            val notificationData = PushNotification(NotificationData("New Message",msg),
                            data!!.fcmToken)

                            ApiUtilities.getInstance().sendNotification(
                                notificationData
                            ).enqueue(object :Callback<PushNotification>{
                                override fun onResponse(
                                    call: Call<PushNotification>,
                                    response: Response<PushNotification>
                                ) {
                                    Toast.makeText(this@ChatActivity,"notification sended",Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(call: Call<PushNotification>, t: Throwable) {

                                    Toast.makeText(this@ChatActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
                                }

                            })


//                            Glide.with(context).load(data!!.image).placeholder(R.drawable.person).into(holder.image)

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ChatActivity,error.message, Toast.LENGTH_SHORT).show()
                    }

                }
            )


    }
}