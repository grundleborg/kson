package com.grundleborg.kson

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BasicTests {

    @Test
    fun TestSimpleJson() {
        val input = """{"stringKey":"stringValue","intKey":12345,"boolKey":false,"nullKey":null,"listKey":[1,2,3,4]}"""
        val parser = JsonParser(input)
        val output = parser.parse()

        assertThat(true).isEqualTo(output.value as Map<JsonKey, JsonValue> != null)
    }

}
