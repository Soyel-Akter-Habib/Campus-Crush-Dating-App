package com.example.sltchat.frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sltchat.R
import com.example.sltchat.adapter.MessageUserAdapter
import com.example.sltchat.databinding.FragmentMessageBinding
import com.example.sltchat.frags.HomeFragment.Companion.list


class MessageFragment : Fragment() {

private lateinit var binding : FragmentMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)

        binding.recyclerView.adapter = MessageUserAdapter(requireContext(),list!!)
        return binding.root
    }

}