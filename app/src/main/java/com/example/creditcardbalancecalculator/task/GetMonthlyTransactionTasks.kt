package com.example.creditcardbalancecalculator.task

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.creditcardbalancecalculator.data.Dataloader
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import com.example.creditcardbalancecalculator.data.db.AppDatabase
import com.example.creditcardbalancecalculator.data.db.dao.MonthlyTransactionDAO

class GetMonthlyTransactionTasks(val context: Context):AsyncTask<Void, MonthlyTransactionList, MonthlyTransactionList>(){
    override fun doInBackground(vararg params: Void?): MonthlyTransactionList {
        val monthlyTransactionDAO = AppDatabase.getDatabase(context).monthlyTransactionDAO()
        if (monthlyTransactionDAO.getAll().isEmpty())
            Dataloader.initMonthlyTransaction()
        return MonthlyTransactionList(ArrayList(monthlyTransactionDAO.getAll()))
    }

}