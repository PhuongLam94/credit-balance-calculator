package com.example.creditcardbalancecalculator.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonthlyTransaction(
    @PrimaryKey val id:String,
    @ColumnInfo(name = "date") val date: Int,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "amount") val amount:Long
) {

}