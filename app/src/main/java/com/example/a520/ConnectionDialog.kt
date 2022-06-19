package com.example.a520

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

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