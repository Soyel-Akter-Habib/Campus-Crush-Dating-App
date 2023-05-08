package com.example.sltchat.frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.sltchat.R
import com.example.sltchat.adapter.DatingAdapter
import com.example.sltchat.databinding.FragmentHomeBinding
import com.example.sltchat.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class HomeFragment : Fragment() {

    private lateinit var binding :FragmentHomeBinding
    private lateinit var manager : CardStackLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =FragmentHomeBinding.inflate(layoutInflater)
        getFirebasedata()
        return binding.root
    }

    private fun manage() {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

                if(manager!!.topPosition == list!!.size){
                    Toast.makeText(requireContext(),"You have swiped all crushes",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.5f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
    }

    companion object{
        var list :ArrayList<UserModel>? = null

    }
    private fun getFirebasedata() {
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object  :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        list = arrayListOf()
                        for(data in snapshot.children){
                            val m = data.getValue(UserModel::class.java)
                            list!!.add(m!!)
                        }
                        list!!.shuffle()
                        manage()

                        binding.cardStackView.layoutManager = manager
                        binding.cardStackView.itemAnimator = DefaultItemAnimator()

                        binding.cardStackView.adapter = DatingAdapter(requireContext(),list!!)
                    }else{
                        Toast.makeText(requireContext(),"Semething went wrong",Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
                }

            })
    }


}