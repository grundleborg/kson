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

import com.grundleborg.kson.ObjectMapper
import com.grundleborg.kson.UnderscoreCamelCaseNameMapper
import com.grundleborg.kson.annotations.JsonName
import org.assertj.core.api.Assertions
import org.junit.Test

class ObjectMapperAnnotationTests {

    internal data class JsonNameTestData(
            val field: String,
            @JsonName("something_else_entirely") val anotherField: String
    )

    @Test
    fun `map with JsonName annotation`() {
        val input = """{"field": "contents 1", "something_else_entirely": "contents 2"}"""

        val objectMapper = ObjectMapper()
        val t = objectMapper.parse<JsonNameTestData>(input)

        Assertions.assertThat(t.field).isEqualTo("contents 1")
        Assertions.assertThat(t.anotherField).isEqualTo("contents 2")
    }

    internal data class JsonNameNullableTestData(
            @JsonName("nullable_custom_name") val differentName: String?
    ) {
        val nullableCustomName: String?
            get() {
                return "property with clashing name"
            }
    }

    @Test
    fun `map with JsonName annotation to nullable property`() {
        val input = """{"nullable_custom_name": "some value"}"""

        val objectMapper = ObjectMapper()
        val t = objectMapper.parse<JsonNameNullableTestData>(input)

        Assertions.assertThat(t.differentName).isEqualTo("some value")
        Assertions.assertThat(t.nullableCustomName).isEqualTo("property with clashing name")
    }

    @Test
    fun `map with JsonName annotation and UnderscoreCamalCaseNameMapper`() {
        val input = """{"nullable_custom_name": "some value"}"""

        val objectMapper = ObjectMapper()
        objectMapper.setNameMapper(UnderscoreCamelCaseNameMapper())
        val t = objectMapper.parse<JsonNameNullableTestData>(input)

        Assertions.assertThat(t.differentName).isEqualTo("some value")
        Assertions.assertThat(t.nullableCustomName).isEqualTo("property with clashing name")
    }
}