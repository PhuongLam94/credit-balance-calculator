package com.example.creditcardbalancecalculator.helper

import com.example.creditcardbalancecalculator.data.Constant
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class DateHelper {
    companion object{
        private val LAST_DATE_OF_CYCLE = 22
        fun setDefaultFromTo(fromDateInputView: TextInputEditText, toDateInputView: TextInputEditText){
            var today = LocalDate.now()
            setDefaultFromToOfDate(today, fromDateInputView, toDateInputView)
        }

        private fun setDefaultFromToOfDate(
            date: LocalDate,
            fromDateInputView: TextInputEditText,
            toDateInputView: TextInputEditText
        ) {
            var fromDate: LocalDate? = null
            var toDate: LocalDate? = null
            if (date.dayOfMonth < LAST_DATE_OF_CYCLE) {
                toDate = LocalDate.of(date.year, date.month, LAST_DATE_OF_CYCLE)
                fromDate = toDate.minusMonths(1).plusDays(1)
            } else {
                fromDate = LocalDate.of(date.year, date.month, LAST_DATE_OF_CYCLE + 1)
                toDate = fromDate.plusMonths(1).minusDays(1)
            }
            fromDateInputView.setText(fromDate.format(Constant.DATE_ONLY_FORMATTER))
            toDateInputView.setText(toDate.format(Constant.DATE_ONLY_FORMATTER))
        }

    }

}
