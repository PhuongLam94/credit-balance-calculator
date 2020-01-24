package com.example.creditcardbalancecalculator.helper

import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import com.example.creditcardbalancecalculator.data.db.dao.MonthlyTransactionDAO
import com.example.creditcardbalancecalculator.data.db.entity.MonthlyTransaction
import java.lang.Exception

class MonthlyTransactionHelper {
    companion object{
        val getAllTransaction : (MonthlyTransactionDAO) -> MonthlyTransactionList = { dao: MonthlyTransactionDAO ->
            MonthlyTransactionList(ArrayList(dao.getAll()))
        }
        val saveTransaction : (MonthlyTransaction) -> ((MonthlyTransactionDAO) -> Unit) = {
            { dao:MonthlyTransactionDAO ->
                dao.insert(it)
            }
        }

        val deleteTransaction: (Int) -> ((MonthlyTransactionDAO) -> Unit) = {id ->
            { dao: MonthlyTransactionDAO ->
                dao.deleteById(id)
            }

        }
        fun validateMonthlyTransactionFields(description: String, amount: String, date: String):String{
            val descriptionMsg = validateDescriptionField(description)
            val amountMsg = validateAmountField(amount)
            val dateMsg = validateDateField(date)
            return descriptionMsg + (if (descriptionMsg.isNotEmpty()) "\n" else "") + amountMsg + (if (amountMsg.isNotEmpty()) "\n" else "")+ dateMsg
        }

        private fun validateDateField(date: String): String {
            try {
                var dateInt = date.toInt()
                if (dateInt in (1..31))
                    return ""
                else
                    return "Date must be between 1 and 31!"
            } catch (e:Exception){
                return "Date must be a number!"
            }
        }

        private fun validateAmountField(amount: String): String {
            try {
                var amountLong = amount.toLong()
                if (amountLong in (0..1000000000))
                    return ""
                else
                    return "Amount must be between 0 and 1B!"
            } catch (e:Exception){
                return "Amount must be a number!"
            }
        }

        private fun validateDescriptionField(description: String): String {
            if (description.length <= 256)
                return ""
            else
                return "Description length cannot exceed 256!"
        }

        val updateTransaction: (MonthlyTransaction) -> ((MonthlyTransactionDAO) -> Unit) = {
            { dao:MonthlyTransactionDAO ->
                dao.update(it)
            }
        }

        val getTransaction : (Int) -> ((MonthlyTransactionDAO) -> MonthlyTransaction) = {id ->
            { dao: MonthlyTransactionDAO ->
                dao.selectById(id)
            }

        }

    }
}