package com.example.sltchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sltchat.databinding.MessageUserBinding
import com.example.sltchat.databinding.UserLayoutBinding
import com.example.sltchat.model.UserModel

class MessageUserAdapter(val context : Context, val list : ArrayList<UserModel>) : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    inner class MessageUserViewHolder(val binding : MessageUserBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {

        return MessageUserViewHolder(MessageUserBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        Glide.with(context).load(list[position].image).into(holder.binding.messageUserImage)
        holder.binding.messageUserName.text = list[position].name
    }

    override fun getItemCount(): Int {

        return list.size
    }
}