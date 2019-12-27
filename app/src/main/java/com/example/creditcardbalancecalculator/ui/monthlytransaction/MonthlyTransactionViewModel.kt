package com.example.creditcardbalancecalculator.ui.monthlytransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthlyTransactionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is monthly transaction Fragment"
    }
    val text: LiveData<String> = _text
}