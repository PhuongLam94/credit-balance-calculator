package com.example.creditcardbalancecalculator.data

import java.time.LocalDateTime

class Transaction(
     var dateTime: LocalDateTime,
     var description: String,
    var amount: Long
) {
    var isMonthlyTransaction = false
}