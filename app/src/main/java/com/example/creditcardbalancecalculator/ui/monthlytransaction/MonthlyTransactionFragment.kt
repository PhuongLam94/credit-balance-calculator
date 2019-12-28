package com.example.creditcardbalancecalculator.ui.monthlytransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.creditcardbalancecalculator.R
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import com.example.creditcardbalancecalculator.task.GetMonthlyTransactionTasks
import com.example.creditcardbalancecalculator.ui.balance.TransactionToTableRowPopulator

class MonthlyTransactionFragment : Fragment() {

    private lateinit var monthlyTransactionViewModel: MonthlyTransactionViewModel
    private lateinit var transactionTable: TableLayout
    private lateinit var transactionToTableRowPopulator: MonthlyTransactionToTableRowPopulator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setViewModel()
        setObserverForLiveData()
        val root = createRoot(inflater, container)
        setViews(root)
        getMonthlyTransactions()
        return root
    }

    private fun getMonthlyTransactions() {
        monthlyTransactionViewModel.monthlyTransactionList.value = GetMonthlyTransactionTasks(context!!.applicationContext).execute().get()
    }

    private fun setViewModel() {
        monthlyTransactionViewModel =
            ViewModelProviders.of(this).get(MonthlyTransactionViewModel::class.java)
    }

    private fun setObserverForLiveData() {
        // Create the observer which updates the UI.
        val monthlyTransactionObserver = Observer<MonthlyTransactionList> { monthlyTransactionList ->
            populateTransactionList(monthlyTransactionList)
        }
        monthlyTransactionViewModel.monthlyTransactionList.observe(this, monthlyTransactionObserver)

    }

    private fun populateTransactionList(monthlyTransactionList: MonthlyTransactionList) {
        clearTransactionTable()
        if (!this::transactionToTableRowPopulator.isInitialized)
            transactionToTableRowPopulator =
                MonthlyTransactionToTableRowPopulator(context, transactionTable, monthlyTransactionList)

        transactionToTableRowPopulator.monthlyTransactionList = monthlyTransactionList
        transactionToTableRowPopulator.populateDataToMonthlyTransactionTable()
    }

    private fun createRoot(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return inflater.inflate(R.layout.fragment_monthly_transaction, container, false)
    }

    private fun setViews(root: View) {
        transactionTable = root.findViewById(R.id.transactionTable)
    }

    private fun clearTransactionTable() {
        while (transactionTable.childCount > 1){
            var rowToRemove = transactionTable.getChildAt(1)
            transactionTable.removeView(rowToRemove)
        }
    }

}