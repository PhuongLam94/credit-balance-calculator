package com.example.creditcardbalancecalculator.data

import androidx.databinding.BaseObservable

class TransactionList(val transactions: ArrayList<Transaction>){
    companion object{
        private val LONG_NOT_INITIALIZED = -1L
    }
    fun addTransactions(transactions: List<Transaction>) {
        this.transactions.addAll(transactions)
    }
    var inAmount = LONG_NOT_INITIALIZED
    var outAmount = LONG_NOT_INITIALIZED
    var totalToPayAmount = LONG_NOT_INITIALIZED
    fun calculateAmounts() {
        calculateInAmount()
        calculateOutAmount()
        totalToPayAmount = outAmount - inAmount
    }

    fun calculateInAmount(): Long {
        if (inAmount == LONG_NOT_INITIALIZED) {

            inAmount = 0L
            for (transaction in transactions) {
                if (transaction.amount < 0)
                    inAmount += (-transaction.amount)
            }
        }
        return inAmount
    }

    fun calculateOutAmount(): Long {
        if (outAmount == LONG_NOT_INITIALIZED) {

            outAmount = 0L
            for (transaction in transactions) {
                if (transaction.amount > 0)
                    outAmount += transaction.amount
            }
        }
        return outAmount
    }

}