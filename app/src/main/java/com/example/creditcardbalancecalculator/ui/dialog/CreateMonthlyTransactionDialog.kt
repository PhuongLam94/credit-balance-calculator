package com.example.creditcardbalancecalculator.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.creditcardbalancecalculator.R
import com.example.creditcardbalancecalculator.data.db.AppDatabase
import com.example.creditcardbalancecalculator.data.db.entity.MonthlyTransaction
import com.google.android.material.textfield.TextInputEditText

class CreateMonthlyTransactionDialog : DialogFragment() {
   interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, description: String, amount: String, date: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val root = inflater.inflate(R.layout.dialog_create_monthly_transaction, null)
            builder.setView(root)
                // Add action buttons
                .setPositiveButton(
                    R.string.create
                ) { dialog, _ ->
                    var listener = targetFragment as NoticeDialogListener
                    var description = root.findViewById<TextInputEditText>(R.id.description).text.toString()
                    var amount = root.findViewById<TextInputEditText>(R.id.amount).text.toString()
                    var date = root.findViewById<TextInputEditText>(R.id.date).text.toString()
                    listener.onDialogPositiveClick(this, description, amount, date)
                }
                .setNegativeButton(
                    R.string.cancel
                ) { _, _ -> dialog.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
