package com.playlab.testingapp

import com.google.common.truth.Truth
import org.junit.Test

class HomeworkTest {
    @Test
    fun `returns the n-th fibonacci number`(){
        val result = Homework.fib(10)

        Truth.assertThat(result).isEqualTo(55)
    }

    @Test
    fun `incorrectly set braces returns false`(){
        val result1 = Homework.checkBraces("((lala)")
        val result2 = Homework.checkBraces(")lala(")
        val result3 = Homework.checkBraces("((lala)(")

        Truth.assertThat(result1).isFalse()
        Truth.assertThat(result2).isFalse()
        Truth.assertThat(result3).isFalse()
    }
}