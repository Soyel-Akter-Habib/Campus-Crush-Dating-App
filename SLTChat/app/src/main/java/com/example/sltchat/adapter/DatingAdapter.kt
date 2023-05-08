package com.example.sltchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sltchat.databinding.UserLayoutBinding
import com.example.sltchat.model.UserModel
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.example.sltchat.activity.ChatActivity

class DatingAdapter(val context : android.content.Context, val list:ArrayList<UserModel>) :
    RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class  DatingViewHolder(val binding : UserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {

        return DatingViewHolder(UserLayoutBinding.inflate(LayoutInflater.from(context),parent ,false))
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {

        holder.binding.frontName.text = list[position].name
        holder.binding.frontEmail.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.imageofUser)

        holder.binding.chatting.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId",list[position].number)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {

        return  list.size
    }
}