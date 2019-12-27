package com.example.creditcardbalancecalculator.processor

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.creditcardbalancecalculator.data.Transaction
import com.example.creditcardbalancecalculator.helper.CursorHelper
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class TransactionProcessor(context: Context?, fromDate: LocalDate, toDate: LocalDate) {
    companion object {
        private val CREDIT_SPENT_MESSAGE_REGEX_1 =
            Regex("(\\d{2}\\/\\d{2}\\/\\d{2} \\d{2}:\\d{2}) the VS Gold 4697 giao dich ([\\d,]+)VND tai (.*)So du kha dung: .*\\.LH \\+842835266060")
        private val CREDIT_SPENT_MESSAGE_REGEX_2 =
            Regex("(\\d{2}\\/\\d{2}\\/\\d{2} \\d{2}:\\d{2}) the VS Gold 4697 giao dich .*\\(tam tinh ([\\d,]+)VND\\) tai (.*)So du kha dung: .*\\.LH \\+842835266060")
        private val CREDIT_REFUND_MESSAGE_REGEX_1 =
            Regex("(\\d{2}\\/\\d{2}\\/\\d{2}) Hoan tien ([\\d,]+)VND giao dich the .* tai (.*) LH 1900555588\\/\\+842835266060")
        private val CREDIT_REFUND_MESSAGE_REGEX_2 =
            Regex("(\\d{2}\\/\\d{2}\\/\\d{2} \\d{2}:\\d{2}) hoan tien giao dich ([\\d,]+)VND cua the VS Gold 4697 .* tai (.*) LH 1900555588\\/\\+842835266060")
        private val MSG_DATE_IDX = 1
        private val MSG_AMOUNT_IDX = 2
        private val MSG_DESCRIPTION_IDX = 3
        private val DATE_TIME_STRING_LENGTH = 14
        private val MSG_DATE_TIME_FORMATTER =
            DateTimeFormatterBuilder().appendPattern("dd/MM/yy HH:mm")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 12)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter()
        private val MSG_DATE_FORMATTER = DateTimeFormatterBuilder().appendPattern("dd/MM/yy")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 12)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter()
        private val MSG_PARSE_EXCEPTION = {body:String -> Exception("This is not a credit card body message: $body") }
        private val LONG_NOT_INITIALIZED = -1L
        class Message(
            val content: String,
            val matchResult: MatchResult,
            val spentMsg: Boolean,
            val dateTimeEpoch: Long
        ) {
        }
    }

    val context = context
    val fromDate = fromDate
    val toDate = toDate

    lateinit var transactions: List<Transaction>
    lateinit var cursor: Cursor

    var inAmount = LONG_NOT_INITIALIZED
    var outAmount = LONG_NOT_INITIALIZED

    var messages = ArrayList<Message>()

    fun calculateTotalToPayAmount(): Long {
        calculateInAmount()
        calculateOutAmount()
        return outAmount - inAmount
    }

    fun calculateInAmount(): Long {
        if (inAmount == LONG_NOT_INITIALIZED && this::transactions.isInitialized) {

            inAmount = 0L
            for (transaction in transactions) {
                if (transaction.amount < 0)
                    inAmount += (-transaction.amount)
            }
        }
        return inAmount
    }

    fun calculateOutAmount(): Long {
        if (outAmount == LONG_NOT_INITIALIZED && this::transactions.isInitialized) {

            outAmount = 0L
            for (transaction in transactions) {
                if (transaction.amount > 0)
                    outAmount += transaction.amount
            }
        }
        return outAmount
    }

    fun readMessageFromBank(): List<Transaction> {
        var transactions: List<Transaction> = listOf()
        cursor = context!!.contentResolver.query(
            Uri.parse("content://sms/inbox"),null,null,null,null
        ) ?: return transactions
        if (cursor.moveToFirst()) {
            do {
                var message = getMessageInstance()
                if (message != null && isTransactionInTime(message)) {
                    var transaction = getTransactionFromMsg(message)
                    transactions += transaction
                }
            } while (cursor.moveToNext())

        }
        this.transactions = transactions
        return transactions
    }


    fun getTotalToPay(): Long {
        return outAmount - inAmount
    }


    private fun getMessageInstance(): Message? {
        var address = CursorHelper.getStringFromColumnName(CursorHelper.ADDRESS_HEADER, cursor)
        if (address == "Sacombank") {
            var body = CursorHelper.getBody(cursor)
            var message = getMessageInstanceOfBody(body)
            if (message != null){
                messages.add(message)
                return message
            }
        }
        return null
    }

    private fun getMessageInstanceOfBody(body: String): Message? {
        var isSpentMsg = true
        var matchResult = getMatchResultFromAny(body, CREDIT_SPENT_MESSAGE_REGEX_1, CREDIT_SPENT_MESSAGE_REGEX_2)
        if (matchResult == null) {
            matchResult = getMatchResultFromAny(body, CREDIT_REFUND_MESSAGE_REGEX_1, CREDIT_REFUND_MESSAGE_REGEX_2)?:return null
            isSpentMsg = false
        }
        return Message(body, matchResult, isSpentMsg,CursorHelper.getStringFromColumnName(CursorHelper.DATE_HEADER, cursor).toLong())
    }

    private fun getTransactionFromMsg(message: Message): Transaction {

        var dateTimeString = getDataFromMsg(message.matchResult, MSG_DATE_IDX)

        var dateTime =
            if (dateTimeString.length == DATE_TIME_STRING_LENGTH)
                LocalDateTime.parse(dateTimeString, MSG_DATE_TIME_FORMATTER)
            else
                LocalDateTime.parse(dateTimeString, MSG_DATE_FORMATTER)

        var description = getDataFromMsg(message.matchResult, MSG_DESCRIPTION_IDX)
        var amountString = getDataFromMsg(message.matchResult, MSG_AMOUNT_IDX)
        var amount = amountString.replace(",", "").toLong()
        if (!message.spentMsg) amount = -amount
        return Transaction(dateTime, description, amount)
    }
    private fun getMatchResultFromAny(string: String,vararg regexes:Regex):MatchResult? {
        var matchResult:MatchResult? = null
        for (regex in regexes){
            matchResult = regex.matchEntire(string)
            if (matchResult != null)
                break
        }
        return matchResult
    }
    private fun getDataFromMsg(matchResult: MatchResult, idx: Int): String {
        return matchResult.groups[idx]?.value ?: throw MSG_PARSE_EXCEPTION(matchResult.groups[0]!!.value)
    }

    private fun isTransactionInTime(message: Message): Boolean {
        var fromDateTime = fromDate.atStartOfDay()
        var toDateTime = toDate.atTime(23, 59, 59)
        var messageDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.dateTimeEpoch), ZoneId.systemDefault())
        return (messageDateTime.isEqual(fromDateTime) || messageDateTime.isAfter(
            fromDateTime
        ))
                && (messageDateTime.isBefore(toDateTime) || messageDateTime.isEqual(
            toDateTime
        ))
    }
}