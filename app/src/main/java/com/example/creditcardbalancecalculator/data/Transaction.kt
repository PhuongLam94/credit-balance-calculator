package com.example.creditcardbalancecalculator.data

import java.time.LocalDateTime

class Transaction {

    lateinit var dateTime:LocalDateTime
    lateinit var description:String
    var amount:Long = 0

    constructor(dateTime: LocalDateTime, description: String, amount: Long) {
        this.dateTime = dateTime
        this.description = description
        this.amount = amount
    }
}