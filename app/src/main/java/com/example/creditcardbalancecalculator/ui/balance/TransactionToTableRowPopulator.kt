package com.example.creditcardbalancecalculator.ui.balance

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.creditcardbalancecalculator.processor.TransactionProcessor
import java.time.format.DateTimeFormatter


class TransactionToTableRowPopulator(
    context: Context?,
    transactionTable: TableLayout,
    transactions: TransactionProcessor
) {
    val transactionTable = transactionTable
    val transactionProcessor = transactions
    val context = context


    companion object {
        private val TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("hh:mm")
        private val MONTH_DAT_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd")
    }

    fun populateDataToTransactionTable() {
        transactionTable.visibility = View.VISIBLE
        populateTransactionRows()
        populateAmountRows()
    }

    private fun populateTransactionRows() {
        for (transaction in transactionProcessor.transactions) {
            var row = getTableRow()
            addTextViewToTableRow(row, transaction.dateTime.format(MONTH_DAT_ONLY_FORMATTER))
            addTextViewToTableRow(row, transaction.dateTime.format(TIME_ONLY_FORMATTER))
            addTextViewToTableRow(row, transaction.description)
            addTextViewToTableRow(row, toVNDFormat(transaction.amount))
            transactionTable.addView(row)
        }
    }

    private fun addTextViewToTableRow(row: TableRow, text: String) {
        addTextViewToTableRow(row, text, getTableCellLayoutParams())
    }

    private fun addTextViewToTableRow(
        row: TableRow, text: String, layoutParams: TableRow.LayoutParams
    ) {
        var textView = TextView(context)
        textView.setText(text)
        textView.layoutParams = layoutParams
        textView.setTextColor(Color.BLACK)
        row.addView(textView)
    }

    private fun populateAmountRows() {
        populateAmountRow("In", transactionProcessor.calculateInAmount())
        populateAmountRow("Out", transactionProcessor.calculateOutAmount())
        populateAmountRow("Total to pay", transactionProcessor.calculateTotalToPayAmount())
    }

    private fun populateAmountRow(title: String, amount: Long) {
        var row = getTableRow()
        var titleLayoutParams = getTableCellLayoutParams(2)
        var amountLayoutParams = getTableCellLayoutParams(2)
        addTextViewToTableRow(row, title, titleLayoutParams)
        addTextViewToTableRow(row, toVNDFormat(amount), amountLayoutParams)
        transactionTable.addView(row)
    }

    private fun getTableRow(): TableRow {
        var row = TableRow(context)
        row.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        return row
    }

    private fun getTableCellLayoutParams(): TableRow.LayoutParams {
        return getTableCellLayoutParams(1)
    }

    private fun getTableCellLayoutParams(spanColumn: Int): TableRow.LayoutParams {
        var layoutParams = TableRow.LayoutParams()
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            context!!.resources.displayMetrics
        ).toInt()
        layoutParams.marginStart = px
        layoutParams.span = spanColumn
        return layoutParams
    }

    private fun toVNDFormat(amount: Long) = String.format("%,d", amount)


}