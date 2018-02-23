package com.grundleborg.kson.parser

import com.grundleborg.kson.JsonParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NumberTests {

    @Test
    fun TestInteger() {
        val input = "123"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Int
        assertThat(out).isEqualTo(123)
    }

    @Test
    fun TestFloat() {
        val input = "2.0E0"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(2.0f)
    }

    @Test
    fun TestLong() {
        val input = "123123123123123123"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Long
        assertThat(out).isEqualTo(123123123123123123)
    }

    @Test
    fun TestDouble() {
        val input = "90.0E99"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Double
        assertThat(out).isEqualTo(90.0E99)
    }

    @Test
    fun TestNegativeInteger() {
        val input = "-123"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Int
        assertThat(out).isEqualTo(-123)
    }

    @Test
    fun TestNegativeFloat() {
        val input = "-2.0E0"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(-2.0f)
    }

    @Test
    fun TestNegativeLong() {
        val input = "-123123123123123123"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Long
        assertThat(out).isEqualTo(-123123123123123123)
    }

    @Test
    fun TestNegativeDouble() {
        val input = "-90.0E99"
        val parser = JsonParser(input)
        val output = parser.parse()

        val out = output.value as Double
        assertThat(out).isEqualTo(-90.0E99)
    }
}
