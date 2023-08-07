package com.example.sltchat.authen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sltchat.MainActivity
import com.example.sltchat.R
import com.example.sltchat.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    val authen = FirebaseAuth.getInstance()
    private var verificationId :String? =null
    private lateinit var dialog :AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myLoadingButton.setOnClickListener{

            if(binding.userNumber.text!!.isEmpty()){
                binding.userNumber.error = "Please enter number"
            }else{
                sendOTP(binding.userNumber.text.toString())
            }
        }

        binding.myLoadingButton2.setOnClickListener{

            if(binding.userOTP.text!!.isEmpty()){
                binding.userOTP.error = "Please enter OTP"
            }else{
                verifyOTP(binding.userOTP.text.toString())
            }
        }

    }

    private fun verifyOTP(otp: String) {
        dialog.show()

        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
//        binding.myLoadingButton2?.showLoadingButton()
        signInWithPhoneAuthCredential(credential)

    }

    private fun sendOTP(number: String) {
//        binding.myLoadingButton?.showLoadingButton()
        dialog.show()

       val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                binding.myLoadingButton?.showNormalButton()

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {


            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginActivity.verificationId=verificationId

                dialog.dismiss()
//                binding.myLoadingButton?.showNormalButton()
                binding.numberView.visibility=GONE
                binding.otpView.visibility = VISIBLE
            }
        }

        val options = PhoneAuthOptions.newBuilder(authen)
            .setPhoneNumber("+91$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        authen.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
//                binding.myLoadingButton2?.showNormalButton()
                if (task.isSuccessful) {
                    checkUserExist(binding.userNumber.text.toString())
//                    startActivity(Intent(this,MainActivity::class.java))
//                    finish()
                } else {
                    dialog.dismiss()
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserExist(number: String) {
        FirebaseDatabase.getInstance().getReference("users").child("+91$number")  //added +91 in the number
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()

                    }else{
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity,error.message,Toast.LENGTH_SHORT).show()
                }

            })
    }
}