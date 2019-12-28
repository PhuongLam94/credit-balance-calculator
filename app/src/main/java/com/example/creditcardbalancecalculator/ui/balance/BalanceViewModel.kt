package com.example.creditcardbalancecalculator.ui.balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import com.example.creditcardbalancecalculator.data.TransactionList

class BalanceViewModel : ViewModel() {

    val monthlyTransactions : MutableLiveData<MonthlyTransactionList> by lazy {
        MutableLiveData<MonthlyTransactionList>()
    }
    val transactionList : MutableLiveData<TransactionList> by lazy {
        MutableLiveData<TransactionList>()
    }
    val transactionHasMonthly : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}