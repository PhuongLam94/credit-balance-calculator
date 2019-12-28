package com.example.creditcardbalancecalculator.ui.monthlytransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.creditcardbalancecalculator.R

class MonthlyTransactionFragment : Fragment() {

    private lateinit var monthlyTransactionViewModel: MonthlyTransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        monthlyTransactionViewModel =
            ViewModelProviders.of(this).get(MonthlyTransactionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_monthly_transaction, container, false)

        return root
    }
}