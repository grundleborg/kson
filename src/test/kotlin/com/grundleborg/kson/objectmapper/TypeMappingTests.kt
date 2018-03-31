package com.grundleborg.kson.objectmapper

import com.grundleborg.kson.ObjectMapper
import com.grundleborg.kson.UnderscoreCamelCaseNameMapper
import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.StringReader

class TypeMappingTests {

    internal data class DataClass(
            val item: String
    )

    internal data class DataClassCamelCase(
            val itemCamelCase: String
    )

    @Test
    fun `Java type`() {
        val input = """{"item":"value"}"""
        val mapper = ObjectMapper()
        mapper.registerType(DataClass::class)

        val t = mapper.parse<DataClass>(input, DataClass::class.java)
        Assertions.assertThat(t.item).isEqualTo("value")
    }

    @Test
    fun `Java type using reader`() {
        val input = """{"item":"value"}"""
        val mapper = ObjectMapper()
        mapper.registerType(DataClass::class)

        val t = mapper.parse<DataClass>(StringReader(input), DataClass::class.java)
        Assertions.assertThat(t.item).isEqualTo("value")
    }

    @Test
    fun `non-default name mapper`() {
        val input = """{"item_camel_case":"value"}"""
        val mapper = ObjectMapper()
        mapper.setNameMapper(UnderscoreCamelCaseNameMapper())

        val t = mapper.parse(StringReader(input), DataClassCamelCase::class)
        Assertions.assertThat(t.itemCamelCase).isEqualTo("value")
    }
}

