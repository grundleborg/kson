package com.grundleborg.kson

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BasicTests {

    @Test
    fun TestSimpleJson() {
        val input = """{"stringKey":"stringValue","intKey":12345,"boolKey":false,"nullKey":null,"listKey":[1,2,3,4]}"""
        val parser = JsonParser(input)
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

        assertThat(list.get(0)!!.value as Int == 1)
        assertThat(list.get(1)!!.value as Int == 2)
        assertThat(list.get(2)!!.value as Int == 3)
        assertThat(list.get(3)!!.value as Int == 4)
    }

    @Test
    fun TestWithTypicalSpaces() {
        val input = """{"stringKey": "stringValue", "intKey": 12345, "boolKey": false, "nullKey": null, "listKey": [1, 2, 3, 4]}"""
        val parser = JsonParser(input)
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

        assertThat(list.get(0)!!.value as Int == 1)
        assertThat(list.get(1)!!.value as Int == 2)
        assertThat(list.get(2)!!.value as Int == 3)
        assertThat(list.get(3)!!.value as Int == 4)
    }

    @Test
    fun TestWithSpacesAllOverThePlace() {
        val input = """  { "stringKey"  : "stringValue" , "intKey"  : 12345 , "boolKey" : false   , "nullKey" : null  , "listKey" :   [ 1   ,  2 ,  3 , 4 ]   }  """
        val parser = JsonParser(input)
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

        assertThat(list.get(0)!!.value as Int == 1)
        assertThat(list.get(1)!!.value as Int == 2)
        assertThat(list.get(2)!!.value as Int == 3)
        assertThat(list.get(3)!!.value as Int == 4)
    }

    @Test
    fun TestWithOtherWhitespaceChars() {
        val input = """

              { "stringKey"         :
              "stringValue" ,
              "intKey"  : 12345 , "boolKey" : false   , "nullKey" : null  ,     "listKey" :   [     1   ,
              2
              ,
               3 , 4 ]   }  """
                .trimIndent()
        val parser = JsonParser(input)
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

        assertThat(list.get(0)!!.value as Int == 1)
        assertThat(list.get(1)!!.value as Int == 2)
        assertThat(list.get(2)!!.value as Int == 3)
        assertThat(list.get(3)!!.value as Int == 4)
    }
}
