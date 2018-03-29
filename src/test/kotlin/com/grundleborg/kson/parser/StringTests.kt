/*
 * Copyright 2018 George Goldberg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
