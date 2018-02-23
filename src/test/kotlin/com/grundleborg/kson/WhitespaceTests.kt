package com.grundleborg.kson

import org.assertj.core.api.Assertions
import org.junit.Test

class WhitespaceTests {

    @Test
    fun TestNoWhitespace() {
        val input = """{"stringKey":"stringValue","intKey":12345,"boolKey":false,"nullKey":null,"listKey":[1,2,3,4]}"""
        val parser = JsonParser(input)
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
    fun TestWithTypicalSpaces() {
        val input = """{"stringKey": "stringValue", "intKey": 12345, "boolKey": false, "nullKey": null, "listKey": [1, 2, 3, 4]}"""
        val parser = JsonParser(input)
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
    fun TestWithSpacesAllOverThePlace() {
        val input = """  { "stringKey"  : "stringValue" , "intKey"  : 12345 , "boolKey" : false   , "nullKey" : null  , "listKey" :   [ 1   ,  2 ,  3 , 4 ]   }  """
        val parser = JsonParser(input)
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