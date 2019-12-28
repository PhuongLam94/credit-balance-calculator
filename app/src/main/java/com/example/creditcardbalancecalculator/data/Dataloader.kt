package com.example.creditcardbalancecalculator.data

import com.example.creditcardbalancecalculator.data.db.AppDatabase
import com.example.creditcardbalancecalculator.data.db.dao.MonthlyTransactionDAO
import com.example.creditcardbalancecalculator.data.db.entity.MonthlyTransaction
import java.util.*

class Dataloader {
    companion object{
        fun initMonthlyTransaction(){
            val monthlyTransactionDAO = AppDatabase.getDatabase().monthlyTransactionDAO()
            monthlyTransactionDAO.insert(MonthlyTransaction(UUID.randomUUID().toString(), 22,"Tra gop Tiki iPad", 557000))
            monthlyTransactionDAO.insert(MonthlyTransaction(UUID.randomUUID().toString(), 22,"Tra gop Tiki Mi 9 SE", 583000))
            monthlyTransactionDAO.insert(MonthlyTransaction(UUID.randomUUID().toString(), 22,"Tra gop Tiki Amazfit", 290000))
        }
    }
}
