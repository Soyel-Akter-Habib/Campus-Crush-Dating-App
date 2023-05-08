package com.example.sltchat.frags

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.sltchat.R
import com.example.sltchat.activity.EditProfile
import com.example.sltchat.authen.LoginActivity
import com.example.sltchat.databinding.FragmentProfileBinding
import com.example.sltchat.model.UserModel
import com.example.sltchat.utilities.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {


    private lateinit var binding :FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Config.showDialog(requireContext())

        binding = FragmentProfileBinding.inflate(layoutInflater)

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val data = it.getValue(UserModel::class.java)

                    binding.nameInProfile.setText(data!!.name.toString())
                    binding.emailInProfile.setText(data!!.email.toString())
                    binding.locationInProfile.setText(data!!.location.toString())
                    binding.numberInProfile.setText(data!!.number.toString())

                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.person).into(binding.userImageInProfile)

                    Config.hideDialog()
                }
            }


        binding.logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            requireActivity().finish()
        }


        binding.editProfile.setOnClickListener{
            startActivity(Intent(requireContext(),EditProfile::class.java))
        }
        return binding.root
    }

}