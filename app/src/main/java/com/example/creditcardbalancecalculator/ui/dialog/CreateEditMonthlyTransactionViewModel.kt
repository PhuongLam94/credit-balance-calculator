package com.example.creditcardbalancecalculator.ui.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList

class CreateEditMonthlyTransactionViewModel : ViewModel(){
    val description : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val amount : MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }
    val date : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
}