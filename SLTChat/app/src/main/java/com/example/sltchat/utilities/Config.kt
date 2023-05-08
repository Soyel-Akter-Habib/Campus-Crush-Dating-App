package com.example.sltchat.utilities

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.sltchat.R
import com.example.sltchat.authen.RegisterActivity
import com.google.firebase.database.core.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Config {
    private var dialog :AlertDialog? =null

    fun showDialog(context : android.content.Context){
        dialog = MaterialAlertDialogBuilder(context)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        dialog!!.show()
    }

    fun hideDialog(){
        dialog!!.dismiss()
    }
}