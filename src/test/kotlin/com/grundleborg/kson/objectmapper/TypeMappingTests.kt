package com.grundleborg.kson.objectmapper

import com.grundleborg.kson.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.StringReader

class TypeMappingTests {

    internal data class DataClass(
            val item: String
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
}

