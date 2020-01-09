package com.example.creditcardbalancecalculator.task

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.creditcardbalancecalculator.data.Dataloader
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import com.example.creditcardbalancecalculator.data.db.AppDatabase
import com.example.creditcardbalancecalculator.data.db.dao.MonthlyTransactionDAO

class ExecuteMonthlyTransactionDBTask<T>(val context: Context):AsyncTask<(MonthlyTransactionDAO) -> T, T, T>(){
    override fun doInBackground(vararg params: (MonthlyTransactionDAO) -> T): T {
        val monthlyTransactionDAO = AppDatabase.getDatabase(context).monthlyTransactionDAO()
        if (monthlyTransactionDAO.getAll().isEmpty())
            Dataloader.initMonthlyTransaction()
        return params[0](monthlyTransactionDAO)
    }

}