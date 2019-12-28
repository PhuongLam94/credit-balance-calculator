package com.example.creditcardbalancecalculator.ui.monthlytransaction

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import java.time.format.DateTimeFormatter


class MonthlyTransactionToTableRowPopulator(
    val context: Context?,
    val monthlyTransactionTable: TableLayout,
    val monthlyTransactionList: MonthlyTransactionList
) {


    companion object {
        private val TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("hh:mm")
        private val MONTH_DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd")
    }

    fun populateDataToMonthlyTransactionTable() {
        monthlyTransactionTable.visibility = View.VISIBLE
        populateMonthlyTransactionRows()
    }

    private fun populateMonthlyTransactionRows() {
        monthlyTransactionList.monthlyTransactions.sortByDescending { it.date }
        for (monthlyTransaction in monthlyTransactionList.monthlyTransactions) {
            var row = getTableRow()
            addTextViewToTableRow(row, monthlyTransaction.date.toString())
            addTextViewToTableRow(row, monthlyTransaction.description)
            addTextViewToTableRow(row, toVNDFormat(monthlyTransaction.amount))
            monthlyTransactionTable.addView(row)
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