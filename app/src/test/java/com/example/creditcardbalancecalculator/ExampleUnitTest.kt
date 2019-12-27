package com.example.creditcardbalancecalculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var count: Int
        count = 15
        var languageName :String = when {
            count == 42 -> {
                count.inc()
                "Answer is $count"
            }
            count < 32 -> {
               count.dec()
                "Answer is not $count"
            }
            else -> "I don't know"

        }
        println(languageName)
        assertEquals(4, 2 + 2)
    }
    fun generateAnswerString() : String {
        return "abc"
    }
}
