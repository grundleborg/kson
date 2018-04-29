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

class ObjectMapperBasicTests {
    data class Test1(val firstParam: String, val secondParam: String = "default")
    data class Test2(val firstParam: String?, val secondParam: String? = "test")
    data class Test3(val firstParam: Int, val secondParam: Int = 10)
    data class TestList(val foo: List<Int>)

    @Test
    fun TestExperiments0() {
        val input = """{"firstParam": "data goes here"}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test1>(output)
        assertThat(t.firstParam).isEqualTo("data goes here")
        assertThat(t.secondParam).isEqualTo("default")
    }

    @Test
    fun TestExperiments1() {
        val input = """{"firstParam": "data goes here", "secondParam": null}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test1>(output)
        assertThat(t.firstParam).isEqualTo("data goes here")
        assertThat(t.secondParam).isEqualTo("default")
    }

    @Test
    fun TestExperiments2() {
        val input = """{"firstParam": "data goes here"}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test2>(output)
        assertThat(t.firstParam).isEqualTo("data goes here")
        assertThat(t.secondParam).isEqualTo(null)   // Null takes precedence over default for an optional parameter.
    }

    @Test
    fun TestExperiments3() {
        val input = """{"secondParam": null}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test2>(output)
        assertThat(t.firstParam).isEqualTo(null)
        assertThat(t.secondParam).isEqualTo(null)
    }

    @Test
    fun TestExperiments4() {
        val input = """{"firstParam": 123, "secondParam": null}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test3>(output)
        assertThat(t.firstParam).isEqualTo(123)
        assertThat(t.secondParam).isEqualTo(10)
    }

    @Test
    fun TestList() {
        val input = """{"foo": [1, 2, 3, 4]}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<TestList>(output)
        assertThat(t.foo.size).isEqualTo(4)
        assertThat(t.foo[2]).isEqualTo(3)
    }
}