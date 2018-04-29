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

package com.grundleborg.kson.objectmapper

import com.grundleborg.kson.JsonParser
import com.grundleborg.kson.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringReader

class NumberFlexibilityTests {
    data class IntClass(val prop: Int)
    data class LongClass(val prop: Long)
    data class FloatClass(val prop: Float)
    data class DoubleClass(val prop: Double)

    @Test
    fun `decoding an int as an int`() {
        val input = """{"prop": 12345}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<IntClass>(output)

        assertThat(t.prop).isEqualTo(12345)
    }

    @Test(expected = ClassCastException::class)
    fun `decoding a long as an int`() {
        val input = """{"prop": 123456789000}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<IntClass>(output)
    }

    @Test(expected = ClassCastException::class)
    fun `decoding a float as an int`() {
        val input = """{"prop": 1.23e+02}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<IntClass>(output)
    }

    @Test(expected = ClassCastException::class)
    fun `decoding a double as an int`() {
        val input = """{"prop": 1.23e99}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<IntClass>(output)
    }

    @Test
    fun `decoding an int as a long`() {
        val input = """{"prop": 12345}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<LongClass>(output)

        assertThat(t.prop).isEqualTo(12345)
    }

    @Test
    fun `decoding a long as a long`() {
        val input = """{"prop": 123456789000}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<LongClass>(output)

        assertThat(t.prop).isEqualTo(123456789000)
    }

    @Test(expected = ClassCastException::class)
    fun `decoding a float as a long`() {
        val input = """{"prop": 1.23e+02}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<LongClass>(output)
    }

    @Test(expected = ClassCastException::class)
    fun `decoding a double as a long`() {
        val input = """{"prop": 1.23e99}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<LongClass>(output)
    }

    @Test
    fun `decoding an int as a float`() {
        val input = """{"prop": 12345}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<FloatClass>(output)

        assertThat(t.prop).isEqualTo(12345.0f)
    }

    @Test
    fun `decoding a long as a float`() {
        val input = """{"prop": 123456789000}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<FloatClass>(output)

        assertThat(t.prop).isEqualTo(123456789000.0f)
    }

    @Test
    fun `decoding a float as a float`() {
        val input = """{"prop": 1.23e+02}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<FloatClass>(output)
        assertThat(t.prop).isEqualTo(1.23e2f)
    }

    @Test(expected = ClassCastException::class)
    fun `decoding a double as a float`() {
        val input = """{"prop": 1.23e99}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<FloatClass>(output)
    }

    @Test
    fun `decoding an int as a double`() {
        val input = """{"prop": 12345}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<DoubleClass>(output)

        assertThat(t.prop).isEqualTo(12345.0)
    }

    @Test
    fun `decoding a long as a double`() {
        val input = """{"prop": 123456789000}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<DoubleClass>(output)

        assertThat(t.prop).isEqualTo(123456789000.0)
    }

    @Test
    fun `decoding a float as a double`() {
        val input = """{"prop": 1.23e+02}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<DoubleClass>(output)
        assertThat(t.prop).isEqualTo(1.23e2)
    }

    @Test
    fun `decoding a double as a double`() {
        val input = """{"prop": 1.23e99}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<DoubleClass>(output)
        assertThat(t.prop).isEqualTo(1.23e99)
    }
}