package com.grundleborg.kson.objectmapper

import com.grundleborg.kson.JsonParser
import com.grundleborg.kson.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

data class Test1(val firstParam: String, val secondParam: String = "default")
data class Test2(val firstParam: String?, val secondParam: String? = "test")
data class Test3(val firstParam: Int, val secondParam: Int = 10)
data class TestList(val foo: List<Int>)

class ObjectMapperBasicTests {
    @Test
    fun TestExperiments0() {
        val input = """{"firstParam": "data goes here"}"""
        val parser = JsonParser(input)
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test1>(output, Test1::class)
        assertThat(t.firstParam).isEqualTo("data goes here")
        assertThat(t.secondParam).isEqualTo("default")
    }

    @Test
    fun TestExperiments1() {
        val input = """{"firstParam": "data goes here", "secondParam": null}"""
        val parser = JsonParser(input)
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test1>(output, Test1::class)
        assertThat(t.firstParam).isEqualTo("data goes here")
        assertThat(t.secondParam).isEqualTo("default")
    }

    @Test
    fun TestExperiments2() {
        val input = """{"firstParam": "data goes here"}"""
        val parser = JsonParser(input)
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test2>(output, Test2::class)
        assertThat(t.firstParam).isEqualTo("data goes here")
        assertThat(t.secondParam).isEqualTo(null)   // Null takes precedence over default for an optional parameter.
    }

    @Test
    fun TestExperiments3() {
        val input = """{"secondParam": null}"""
        val parser = JsonParser(input)
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test2>(output, Test2::class)
        assertThat(t.firstParam).isEqualTo(null)
        assertThat(t.secondParam).isEqualTo(null)
    }

    @Test
    fun TestExperiments4() {
        val input = """{"firstParam": 123, "secondParam": null}"""
        val parser = JsonParser(input)
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<Test3>(output, Test3::class)
        assertThat(t.firstParam).isEqualTo(123)
        assertThat(t.secondParam).isEqualTo(10)
    }

    @Test
    fun TestList() {
        val input = """{"foo": [1, 2, 3, 4]}"""
        val parser = JsonParser(input)
        val output = parser.parse()

        val objectMapper = ObjectMapper()

        val t = objectMapper.map<TestList>(output, TestList::class)
        assertThat(t.foo.size).isEqualTo(4)
        assertThat(t.foo[2]).isEqualTo(3)
    }
}