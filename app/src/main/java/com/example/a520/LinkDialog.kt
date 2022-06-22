package com.example.a520

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class LinkDialog(var link: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Перейти по $link?")
                .setPositiveButton("Да", activity as DialogInterface.OnClickListener)
                .setNegativeButton("Нет", null)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}