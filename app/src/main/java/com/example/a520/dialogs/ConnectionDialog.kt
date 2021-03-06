package com.example.a520.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.a520.R

class ConnectionDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setIcon(R.drawable.world_news)
                .setTitle("Включите Интернет")
                .setMessage("Для работы приложения требуется наличие сети")
                .setPositiveButton("Понятно", null)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}