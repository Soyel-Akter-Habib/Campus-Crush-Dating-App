package com.example.sltchat.authen

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sltchat.MainActivity
import com.example.sltchat.R
import com.example.sltchat.databinding.ActivityRegisterBinding
import com.example.sltchat.model.UserModel
import com.example.sltchat.utilities.Config
import com.example.sltchat.utilities.Config.hideDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {

private lateinit var binding:ActivityRegisterBinding
private var imgUri :Uri? =null
private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
    imgUri = it
    binding.userImage.setImageURI(imgUri)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.saveProfile.setOnClickListener{
            validateData()
        }


    }

    private fun validateData() {
        if(binding.userName.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || binding.userLocation.text.toString().isEmpty()
            || imgUri ==null){
            Toast.makeText(this,"Please enter all fields",Toast.LENGTH_SHORT).show()

        }else if(!binding.tAndC.isChecked){
            Toast.makeText(this,"Please accept terms and conditions",Toast.LENGTH_SHORT).show()


        } else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

       val storageRef= FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

            storageRef.putFile(imgUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener{
                    hideDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                    hideDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
    }

    private fun storeData(imageUrl: Uri?) {
//        val token :String? =null

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            val data = UserModel(
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString(),
                name = binding.userName.text.toString(),
                image = imageUrl.toString(),
                email = binding.userEmail.text.toString(),
                location = binding.userLocation.text.toString(),
                fcmToken = token



            )


            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
                .setValue(data).addOnCompleteListener{
                    hideDialog()
                    if(it.isSuccessful){
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                        Toast.makeText(this,"User registered sucessfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()

                    }
                }

        })




    }
}