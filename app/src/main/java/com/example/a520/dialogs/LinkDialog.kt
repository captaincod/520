package com.example.a520.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.a520.R

class LinkDialog(var link: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setIcon(R.drawable.link)
                .setMessage("Перейти по $link?")
                .setPositiveButton("Да", activity as DialogInterface.OnClickListener)
                .setNegativeButton("Нет", null)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}