package com.example.creditcardbalancecalculator.data

import java.time.format.DateTimeFormatter

abstract class Constant {
    companion object{
        val EDIT = "edit"
        val CREATE = "create"
        val DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }
}