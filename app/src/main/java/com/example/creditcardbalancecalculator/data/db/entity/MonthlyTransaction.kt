package com.example.creditcardbalancecalculator.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.random.Random

@Entity
data class MonthlyTransaction(
    @PrimaryKey val id:Int ,
    @ColumnInfo(name = "date") val date: Int,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "amount") val amount:Long
) {
    constructor(_date: Int, _description: String, _amount: Long):this(Random.nextInt(), _date, _description, _amount){

    }
}