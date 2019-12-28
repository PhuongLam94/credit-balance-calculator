package com.example.creditcardbalancecalculator.ui.monthlytransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList

class MonthlyTransactionViewModel : ViewModel() {
    val monthlyTransactionList : MutableLiveData<MonthlyTransactionList> by lazy {
        MutableLiveData<MonthlyTransactionList>()
    }
}