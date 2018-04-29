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
import java.io.StringReader

class NumberTests {

    @Test
    fun `int`() {
        val input = "123"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Int
        assertThat(out).isEqualTo(123)
    }

    @Test
    fun `float`() {
        val input = "2.0E0"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(2.0f)
    }

    @Test
    fun `long`() {
        val input = "123123123123123123"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Long
        assertThat(out).isEqualTo(123123123123123123)
    }

    @Test
    fun `double`() {
        val input = "90.0E99"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Double
        assertThat(out).isEqualTo(90.0E99)
    }

    @Test
    fun `decimal`() {
        val input = "8489666.094580958"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(8489666.0f)
    }

    @Test
    fun `positive exponent sign`() {
        val input = "8.489666094580958e+06"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(8489666.0f)
    }

    @Test
    fun `negative exponent sign`() {
        val input = "8.489666094580958e-06"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(8.4896665e-6f)
    }

    @Test
    fun `negative int`() {
        val input = "-123"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Int
        assertThat(out).isEqualTo(-123)
    }

    @Test
    fun `negative float`() {
        val input = "-2.0E0"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(-2.0f)
    }

    @Test
    fun `negative long`() {
        val input = "-123123123123123123"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Long
        assertThat(out).isEqualTo(-123123123123123123)
    }

    @Test
    fun `negative double`() {
        val input = "-90.0E99"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Double
        assertThat(out).isEqualTo(-90.0E99)
    }

    @Test
    fun `negative decimal`() {
        val input = "-8489666.094580958"
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val out = output.value as Float
        assertThat(out).isEqualTo(-8489666.0f)
    }
}
