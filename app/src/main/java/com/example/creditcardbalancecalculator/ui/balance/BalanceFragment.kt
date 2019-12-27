package com.example.creditcardbalancecalculator.ui.balance

import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.creditcardbalancecalculator.data.Transaction
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import android.widget.*
import androidx.core.view.children
import com.example.creditcardbalancecalculator.R
import com.example.creditcardbalancecalculator.data.Constant.Companion.DATE_ONLY_FORMATTER
import com.example.creditcardbalancecalculator.helper.DateHelper
import com.example.creditcardbalancecalculator.processor.TransactionProcessor
import kotlinx.android.synthetic.main.fragment_balance.*


class BalanceFragment : Fragment() {

    private lateinit var balanceViewModel: BalanceViewModel
    private lateinit var fromDateInputView: TextInputEditText
    private lateinit var toDateInputView: TextInputEditText
    private lateinit var selectDateView: CalendarView
    private var currentFocusInput: TextInputEditText? = null
    private lateinit var calculateBtn: Button
    private lateinit var transactionTable:TableLayout

    private lateinit var cursor: Cursor
    companion object {
        private val INPUT_FIELD_DATE_FORMATTER = DATE_ONLY_FORMATTER
    }

    private lateinit var fromDate: LocalDate
    private lateinit var toDate: LocalDate
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setViewModel()
        val root = createRoot(inflater, container)
        setViews(root)
        handleTextInputs()
        handleSelectDate()
        handleCalculateBtn()
        handleTransactionTable()
        return root
    }



    private fun setViewModel() {
        balanceViewModel =
            ViewModelProviders.of(this).get(BalanceViewModel::class.java)
    }

    private fun createRoot(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    private fun setViews(root: View) {
        fromDateInputView = root.findViewById(R.id.fromDateInput)
        toDateInputView = root.findViewById(R.id.toDateInput)
        selectDateView = root.findViewById(R.id.selectDate)
        calculateBtn = root.findViewById(R.id.calculateBtn)
        transactionTable = root.findViewById(R.id.transactionTable)
    }

    private fun handleTextInputs() {
        handleTextInput(fromDateInputView)
        handleTextInput(toDateInputView)
        DateHelper.setDefaultFromTo(fromDateInputView, toDateInputView)
    }

    private fun handleTextInput(input: TextInputEditText) {
        input.showSoftInputOnFocus = false
        input.onFocusChangeListener =
            View.OnFocusChangeListener(this.textInputOnFocusChangeListener)
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                textInputAfterChangeListener()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private val textInputOnFocusChangeListener = { v: View, onFocus: Boolean ->
        if (onFocus) {
            selectDateView.visibility = View.VISIBLE
            transactionTable.visibility = View.INVISIBLE
            selectDate.bringToFront()
            currentFocusInput = v as TextInputEditText
        } else {
            selectDateView.visibility = View.INVISIBLE

        }
    }


    private val textInputAfterChangeListener = {
        calculateBtn.isEnabled =
            isValidDateInput(fromDateInputView) && isValidDateInput(toDateInputView)
        if (calculateBtn.isEnabled) {
            fromDate = getDateFromInput(fromDateInputView)
            toDate = getDateFromInput(toDateInputView)
        }
    }

    private fun getDateFromInput(inputView: TextInputEditText): LocalDate {
        var text = inputView.text
        return LocalDate.parse(text, INPUT_FIELD_DATE_FORMATTER)
            ?: throw Exception("Cannot parse dateTime string")
    }

    private fun isValidDateInput(inputView: TextInputEditText): Boolean {
        try {
            getDateFromInput(inputView)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    private fun handleSelectDate() {
        selectDateView.visibility = View.INVISIBLE
        selectDateView.setOnDateChangeListener(
            CalendarView.OnDateChangeListener(
                selectDateOnClickListener
            )
        )
    }

    private val selectDateOnClickListener = { _: CalendarView, year: Int, month: Int, day: Int ->
        //month returned from event range is 0..11, while month in Local Date is 1..12, so need to plus 1
        var selectedDate = LocalDate.of(year, month+1, day)
        currentFocusInput!!.setText(selectedDate.format(INPUT_FIELD_DATE_FORMATTER))
    }

    private fun handleCalculateBtn() {
        calculateBtn.setOnClickListener {
            currentFocusInput?.clearFocus()
            clearTransactionTable()
            var transactionProcessor = TransactionProcessor(context, fromDate, toDate)
            transactionProcessor.readMessageFromBank()

            var transactionToTableRowPopulator = TransactionToTableRowPopulator(context, transactionTable, transactionProcessor)
            transactionToTableRowPopulator.populateDataToTransactionTable()

        }
    }

    private fun clearTransactionTable() {
        while (transactionTable.childCount > 1){
            var rowToRemove = transactionTable.getChildAt(1)
            transactionTable.removeView(rowToRemove)
        }
    }

    private fun handleTransactionTable() {
        transactionTable.visibility = View.INVISIBLE
    }
}