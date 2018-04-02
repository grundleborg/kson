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
import com.grundleborg.kson.JsonValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringReader

class BasicTests {

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `basic json object`() {
        val input = """{"stringKey": "stringValue", "intKey": 12345, "boolKey": false, "nullKey": null, "listKey": [1, 2, 3, 4]}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val topLevelObject = (output.value as Map<String, JsonValue>)
        assertThat(topLevelObject).isNotNull
        assertThat(topLevelObject.size).isEqualTo(5)

        assertThat(topLevelObject.get("stringKey")!!.value as String == "stringValue")
        assertThat(topLevelObject.get("intKey")!!.value as Int == 12345)
        assertThat(topLevelObject.get("boolKey")!!.value as Boolean == false)
        assertThat(topLevelObject.get("nullKey")!!.value == null)

        val list = (topLevelObject.get("listKey")!!.value as List<JsonValue>)
        assertThat(list).isNotNull
        assertThat(list.size).isEqualTo(4)

        assertThat(list.get(0).value as Int == 1)
        assertThat(list.get(1).value as Int == 2)
        assertThat(list.get(2).value as Int == 3)
        assertThat(list.get(3).value as Int == 4)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `single string object`() {
        val input = """{"item":"value"}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val topLevelObject = (output.value as Map<String, JsonValue>)
        assertThat(topLevelObject).isNotNull
        assertThat(topLevelObject.size).isEqualTo(1)
    }

    /*
     * This tests for a bug that was fixed in 0.2.2 which meant that any fields after
     * an inner-object weren't parsed and so were missing from the results.
     */
    @Test
    @Suppress("UNCHECKED_CAST")
    fun `check closing child objects`() {
        val input = """{"inner_object": {"field": "value"}, "another_field": "another_value"}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val topLevelObject = (output.value as Map<String, JsonValue>)
        assertThat(topLevelObject).isNotNull
        assertThat(topLevelObject).containsKey("inner_object")
        assertThat(topLevelObject).containsKey("another_field")
        assertThat(topLevelObject["another_field"]!!.value as String).isEqualTo("another_value")

        val innerObject = topLevelObject["inner_object"]!!.value as Map<String, JsonValue>
        assertThat(innerObject["field"]!!.value as String).isEqualTo("value")
    }
}
