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
import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.StringReader

class WhitespaceTests {

    @Test
    fun `no whitespace`() {
        val input = """{"stringKey":"stringValue","intKey":12345,"boolKey":false,"nullKey":null,"listKey":[1,2,3,4]}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val topLevelObject = (output.value as Map<String, JsonValue>)
        Assertions.assertThat(topLevelObject).isNotNull
        Assertions.assertThat(topLevelObject.size).isEqualTo(5)

        Assertions.assertThat(topLevelObject.get("stringKey")!!.value as String == "stringValue")
        Assertions.assertThat(topLevelObject.get("intKey")!!.value as Int == 12345)
        Assertions.assertThat(topLevelObject.get("boolKey")!!.value as Boolean == false)
        Assertions.assertThat(topLevelObject.get("nullKey")!!.value == null)

        val list = (topLevelObject.get("listKey")!!.value as List<JsonValue>)
        Assertions.assertThat(list).isNotNull
        Assertions.assertThat(list.size).isEqualTo(4)

        Assertions.assertThat(list.get(0).value as Int == 1)
        Assertions.assertThat(list.get(1).value as Int == 2)
        Assertions.assertThat(list.get(2).value as Int == 3)
        Assertions.assertThat(list.get(3).value as Int == 4)
    }

    @Test
    fun `typical spaces`() {
        val input = """{"stringKey": "stringValue", "intKey": 12345, "boolKey": false, "nullKey": null, "listKey": [1, 2, 3, 4]}"""
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val topLevelObject = (output.value as Map<String, JsonValue>)
        Assertions.assertThat(topLevelObject).isNotNull
        Assertions.assertThat(topLevelObject.size).isEqualTo(5)

        Assertions.assertThat(topLevelObject.get("stringKey")!!.value as String == "stringValue")
        Assertions.assertThat(topLevelObject.get("intKey")!!.value as Int == 12345)
        Assertions.assertThat(topLevelObject.get("boolKey")!!.value as Boolean == false)
        Assertions.assertThat(topLevelObject.get("nullKey")!!.value == null)

        val list = (topLevelObject.get("listKey")!!.value as List<JsonValue>)
        Assertions.assertThat(list).isNotNull
        Assertions.assertThat(list.size).isEqualTo(4)

        Assertions.assertThat(list.get(0).value as Int == 1)
        Assertions.assertThat(list.get(1).value as Int == 2)
        Assertions.assertThat(list.get(2).value as Int == 3)
        Assertions.assertThat(list.get(3).value as Int == 4)
    }

    @Test
    fun `spaces all over the place`() {
        val input = """  { "stringKey"  : "stringValue" , "intKey"  : 12345 , "boolKey" : false   , "nullKey" : null  , "listKey" :   [ 1   ,  2 ,  3 , 4 ]   }  """
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val topLevelObject = (output.value as Map<String, JsonValue>)
        Assertions.assertThat(topLevelObject).isNotNull
        Assertions.assertThat(topLevelObject.size).isEqualTo(5)

        Assertions.assertThat(topLevelObject.get("stringKey")!!.value as String == "stringValue")
        Assertions.assertThat(topLevelObject.get("intKey")!!.value as Int == 12345)
        Assertions.assertThat(topLevelObject.get("boolKey")!!.value as Boolean == false)
        Assertions.assertThat(topLevelObject.get("nullKey")!!.value == null)

        val list = (topLevelObject.get("listKey")!!.value as List<JsonValue>)
        Assertions.assertThat(list).isNotNull
        Assertions.assertThat(list.size).isEqualTo(4)

        Assertions.assertThat(list.get(0).value as Int == 1)
        Assertions.assertThat(list.get(1).value as Int == 2)
        Assertions.assertThat(list.get(2).value as Int == 3)
        Assertions.assertThat(list.get(3).value as Int == 4)
    }

    @Test
    fun `other whitespace characters`() {
        val input = """

              { "stringKey"         :
              "stringValue" ,
              "intKey"  : 12345 , "boolKey" : false   , "nullKey" : null  ,     "listKey" :   [     1   ,
              2
              ,
               3 , 4 ]   }  """
                .trimIndent()
        val parser = JsonParser(StringReader(input))
        val output = parser.parse()

        val topLevelObject = (output.value as Map<String, JsonValue>)
        Assertions.assertThat(topLevelObject).isNotNull
        Assertions.assertThat(topLevelObject.size).isEqualTo(5)

        Assertions.assertThat(topLevelObject.get("stringKey")!!.value as String == "stringValue")
        Assertions.assertThat(topLevelObject.get("intKey")!!.value as Int == 12345)
        Assertions.assertThat(topLevelObject.get("boolKey")!!.value as Boolean == false)
        Assertions.assertThat(topLevelObject.get("nullKey")!!.value == null)

        val list = (topLevelObject.get("listKey")!!.value as List<JsonValue>)
        Assertions.assertThat(list).isNotNull
        Assertions.assertThat(list.size).isEqualTo(4)

        Assertions.assertThat(list.get(0).value as Int == 1)
        Assertions.assertThat(list.get(1).value as Int == 2)
        Assertions.assertThat(list.get(2).value as Int == 3)
        Assertions.assertThat(list.get(3).value as Int == 4)
    }
}