package com.example.creditcardbalancecalculator.helper

import android.database.Cursor

class CursorHelper {
    companion object {
        val ADDRESS_HEADER = "address"
        val BODY_HEADER = "body"
        val DATE_HEADER = "date"
        fun getStringFromColumnName(columnName:String, cursor: Cursor):String{
            var columnIdx = cursor.getColumnIndex(columnName)
            return cursor.getString(columnIdx)
        }
        fun getBody(cursor: Cursor):String{
           return getStringFromColumnName(BODY_HEADER, cursor)
        }
    }
}