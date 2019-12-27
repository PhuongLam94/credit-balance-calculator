package com.example.creditcardbalancecalculator.data

import java.time.format.DateTimeFormatter

abstract class Constant {
    companion object{
        val DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }
}