package com.example.sltchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sltchat.R
import com.example.sltchat.model.MessageModel
import com.example.sltchat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MessageAdapter(val context : Context, val list : List<MessageModel>)
    :RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    private val msg_right_type =0
    private val msg_left_type =1

    inner class MessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val msg = itemView.findViewById<TextView>(R.id.msgText)
//        val image = itemView.findViewById<ImageView>(R.id.senderImage)
    }

    override fun getItemViewType(position: Int): Int {

        val currentUserPhoneNumber = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        val messageSenderId = list[position].senderId

        return if(currentUserPhoneNumber==messageSenderId){
            msg_right_type
        }else{
            msg_left_type
        }



    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return  if(viewType==msg_right_type){
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false))
        }else{
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false))


        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.msg.text = list[position].message

        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position].senderId!!).addListenerForSingleValueEvent(
                object  : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){
                            val data = snapshot.getValue(UserModel::class.java)

//                            Glide.with(context).load(data!!.image).placeholder(R.drawable.person).into(holder.image)

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context,error.message, Toast.LENGTH_SHORT).show()
                    }

                }
            )

    }

    override fun getItemCount(): Int {
        return list.size
    }
}