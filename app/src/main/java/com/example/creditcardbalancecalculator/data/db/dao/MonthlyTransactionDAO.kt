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

    @Query("DELETE FROM monthlytransaction WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM monthlytransaction WHERE id = :id")
    fun selectById(id: Int) : MonthlyTransaction

    @Update
    fun update(monthlyTransaction: MonthlyTransaction)
}