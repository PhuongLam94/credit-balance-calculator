package com.example.creditcardbalancecalculator.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipDescription
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.creditcardbalancecalculator.R
import com.example.creditcardbalancecalculator.data.Constant
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import com.example.creditcardbalancecalculator.data.db.AppDatabase
import com.example.creditcardbalancecalculator.data.db.entity.MonthlyTransaction
import com.example.creditcardbalancecalculator.ui.monthlytransaction.MonthlyTransactionViewModel
import com.google.android.material.textfield.TextInputEditText

class CreateEditMonthlyTransactionDialog(val mode:String) : DialogFragment() {
   interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, description: String, amount: String, date: String)
       fun onDialogSetViewModel(dialog: CreateEditMonthlyTransactionDialog)
    }

    private lateinit var listener : NoticeDialogListener
    lateinit var  viewModel : CreateEditMonthlyTransactionViewModel
        private set
    private lateinit var descriptionTextInput : TextInputEditText
    private lateinit var amountTextInput : TextInputEditText
    private lateinit var dateTextInput : TextInputEditText
    private lateinit var root : View


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            listener = targetFragment as NoticeDialogListener
            val builder = AlertDialog.Builder(it)
            buildRoot(builder)
            setViewModel()
            val dialog = builder.create()
            setViews()
            setBtnOnClickListenner(dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setBtnOnClickListenner(dialog: AlertDialog) {
        dialog.setOnShowListener {
            val positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveBtn.setOnClickListener()
            {

                val description = descriptionTextInput.text.toString()
                val amount = amountTextInput.text.toString()
                val date = dateTextInput.text.toString()
                listener.onDialogPositiveClick(this, description, amount, date)
            }
        }
    }

    fun setViewModel() {
        viewModel =
            ViewModelProviders.of(this).get(CreateEditMonthlyTransactionViewModel::class.java)
        val descriptionObserver =
            Observer<String> { description ->
                descriptionTextInput.setText(description)
            }
        val amountObserver =
            Observer<Long> { amount ->
                amountTextInput.setText(amount.toString())
            }
        val dateObserver =
            Observer<Int> { date ->
                dateTextInput.setText(date.toString())
            }
        viewModel.description.observe(this, descriptionObserver)
        viewModel.amount.observe(this, amountObserver)
        viewModel.date.observe(this, dateObserver)
        listener.onDialogSetViewModel(this)

    }

    private fun setViews() {
        descriptionTextInput = root.findViewById<TextInputEditText>(R.id.description)
        amountTextInput = root.findViewById<TextInputEditText>(R.id.amount)
        dateTextInput = root.findViewById<TextInputEditText>(R.id.date)
    }

    private fun buildRoot(builder: AlertDialog.Builder) {
        val inflater = requireActivity().layoutInflater;
        root = inflater.inflate(R.layout.dialog_create_monthly_transaction, null)
        builder.setView(root)
            // Add action buttons
            .setPositiveButton(
                if (mode == Constant.CREATE) R.string.create else R.string.edit, null
            )
            .setNegativeButton(
                R.string.cancel
            ) { _, _ -> dialog.cancel() }
    }

}
