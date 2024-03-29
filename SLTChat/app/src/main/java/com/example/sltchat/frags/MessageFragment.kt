package com.example.sltchat.frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sltchat.R
import com.example.sltchat.adapter.MessageUserAdapter
import com.example.sltchat.databinding.FragmentMessageBinding
import com.example.sltchat.frags.HomeFragment.Companion.list
import com.example.sltchat.utilities.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class MessageFragment : Fragment() {

private lateinit var binding : FragmentMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)

        getData()
//        binding.recyclerView.adapter = MessageUserAdapter(requireContext(), list!!)


        return binding.root
    }

    private fun getData() {
        Config.showDialog(requireContext())
        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    var list = arrayListOf<String>()
                    var newList = arrayListOf<String>()

                    for (data in snapshot.children){
                        if (data.key!!.contains(currentId!!)){
                            list.add(data.key!!.replace(currentId!!,""))
                            newList.add(data.key!!)

                        }
                    }

                    try {
                        binding.recyclerView.adapter = MessageUserAdapter(
                            context = requireContext(),
                            list, newList
                        )
                    } catch(e:Exception){}

                    Config.hideDialog()


                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()

                }

            })
    }

}