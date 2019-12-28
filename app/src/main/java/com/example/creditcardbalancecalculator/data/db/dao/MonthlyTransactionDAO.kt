package com.example.creditcardbalancecalculator.data.db.dao

import androidx.room.*
import com.example.creditcardbalancecalculator.data.db.entity.MonthlyTransaction

@Dao
interface MonthlyTransactionDAO {
    @Query("SELECT * FROM monthlytransaction")
    fun getAll(): List<MonthlyTransaction>

    @Delete
    fun delete(monthlyTransaction: MonthlyTransaction)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(monthlyTransaction: MonthlyTransaction)
}