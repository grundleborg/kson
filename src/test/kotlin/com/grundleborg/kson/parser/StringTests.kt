package com.grundleborg.kson.parser

import com.grundleborg.kson.JsonParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringTests {

    @Test
    fun TestSimpleString() {
        val input = """"simpleStringValue""""
        val parser = JsonParser(input)
        val output = parser.parse()

        val outString = output.value as String
        assertThat(outString).isEqualTo("simpleStringValue")
    }

    @Test
    fun TestStringWithSpace() {
        val input = """"simple String  Value""""
        val parser = JsonParser(input)
        val output = parser.parse()

        val outString = output.value as String
        assertThat(outString).isEqualTo("simple String  Value")
    }

    @Test
    fun TestStringWithEscapedDoubleQuotes() {
        val input = """"simpleStringValue\"""""
        val parser = JsonParser(input)
        val output = parser.parse()

        val outString = output.value as String
        assertThat(outString).isEqualTo("simpleStringValue\"")
    }

    @Test
    fun TestStringWithOtherAsciiEscapes() {
        val input = """"s\bi\nm\tp\rl\fe\\StringValue""""
        val parser = JsonParser(input)
        val output = parser.parse()

        val outString = output.value as String
        assertThat(outString).isEqualTo("s\bi\nm\tp\rl"+0x0C.toChar()+"e\\StringValue")
    }

    @Test(expected = Exception::class)
    fun TestInvalidEscapeSequence() {
        val input = """"simple\zStringValue""""
        val parser = JsonParser(input)
        val output = parser.parse()
    }

    @Test
    fun TestStringWithUnicodeEscapeSequences() {
        val input = """"I live in \u9999\u6E2F.""""
        val parser = JsonParser(input)
        val output = parser.parse()

        val outString = output.value as String
        assertThat(outString).isEqualTo("I live in 香港.")
    }
}
