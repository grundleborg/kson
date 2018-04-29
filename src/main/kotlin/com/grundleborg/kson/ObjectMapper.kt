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

package com.grundleborg.kson

import com.grundleborg.kson.annotations.JsonName
import java.io.Reader
import java.io.StringReader
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import java.lang.reflect.Type
import kotlin.reflect.full.findAnnotation

class ObjectMapper {
    private val typeMap: MutableMap<Type, KClass<*>> = mutableMapOf()
    private var nameMapper: NameMapper = SimpleNameMapper()

    /**
     * Parse the provided JSON data and map it into an object of the appropriate type.
     *
     * @param <T> The type of the object to the JSON data should be mapped into.
     * @param json The JSON data to parse and map.
     * @return A new instance of the requested object populated from the provided JSON data.
     */
    inline fun <reified T: Any> parse(json: String): T {
        return parse(json, T::class)
    }

    /**
     * Parse the provided JSON data and map it into an object of the appropriate type.
     *
     * @param <T> The type of the object to the JSON data should be mapped into.
     * @param json The JSON data to parse and map.
     * @return A new instance of the requested object populated from the provided JSON data.
     */
    inline fun <reified T: Any> parse(json: Reader): T {
        return parse(json, T::class)
    }

    /**
     * Parse the provided JSON data and map it into an object of the appropriate type.
     *
     * @param <T> The type of the object to the JSON data should be mapped into.
     * @param json The JSON data to parse and map.
     * @param type The Java meta-class for the <T> type.
     * @return A new instance of the requested object populated from the provided JSON data.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> parse(json: String, type: Type): T {
        return parse(json, typeMap[type]!! as KClass<T>)
    }

    /**
     * Parse the provided JSON data and map it into an object of the appropriate type.
     *
     * @param <T> The type of the object to the JSON data should be mapped into.
     * @param json The JSON data to parse and map.
     * @param type The Java meta-class for the <T> type.
     * @return A new instance of the requested object populated from the provided JSON data.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> parse(json: Reader, type: Type): T {
        return parse(json, typeMap[type]!! as KClass<T>)
    }

    /**
     * Parse the provided JSON data and map it into an object of the appropriate type.
     *
     * @param <T> The type of the object to the JSON data should be mapped into.
     * @param json The JSON data to parse and map.
     * @param cls The Kotlin meta-class for the <T> type.
     * @return A new instance of the requested object populated from the provided JSON data.
     */
    fun <T: Any> parse(json: String, cls: KClass<T>): T {
        val parser = JsonParser(StringReader(json))
        return map(parser.parse(), cls)
    }

    /**
     * Parse the provided JSON data and map it into an object of the appropriate type.
     *
     * @param <T> The type of the object to the JSON data should be mapped into.
     * @param json The JSON data to parse and map.
     * @param cls The Kotlin meta-class for the <T> type.
     * @return A new instance of the requested object populated from the provided JSON data.
     */
    fun <T: Any> parse(json: Reader, cls: KClass<T>): T {
        val parser = JsonParser(json)
        return map(parser.parse(), cls)
    }

    /**
     * Register a type so that it can be used as a target for JSON data mapping when identified
     * only by it's Java Type.
     *
     * @param <T> The type to register.
     * @param <cls> The Kotlin meta-class for the type <T> being registered.
     */
    fun <T: Any> registerType(cls: KClass<T>) {
        typeMap[cls.java] = cls
    }

    /**
     * Sets the name mapper to use for mapping between JSON keys and Kotlin properties.
     *
     * @param nameMapper An instance of the desired NameMapper to use.
     */
    fun setNameMapper(nameMapper: NameMapper) {
        this.nameMapper = nameMapper
    }

    inline fun <reified T: Any> map(jsonValue: JsonValue): T {
        return map(jsonValue, T::class)
    }

    fun <T: Any> map(jsonValue: JsonValue, cls: KClass<T>): T {
        return when (cls) {
            Int::class -> mapInt(jsonValue) as T
            Long::class -> mapLong(jsonValue) as T
            Float::class -> mapFloat(jsonValue) as T
            Double::class -> mapDouble(jsonValue) as T
            Boolean::class -> mapBoolean(jsonValue) as T
            String::class -> mapString(jsonValue) as T
            else -> mapObject(jsonValue, cls)
        }
    }

    private fun <T: Any> mapObject(jsonValue: JsonValue, cls: KClass<T>): T {
        val cons = cls.primaryConstructor!!
        val paramMap = HashMap<KParameter, Any?>()
        val jsonObject = jsonValue.value as Map<String, JsonValue>


        jsonObject.entries.map {
            val unmappedParamName = it.key
            val mappedParamName = nameMapper.fromJson(it.key)

            cons.parameters.forEach { paramDef ->
                if (paramDef.name == null) {
                    return@forEach
                }

                val paramDefName = paramDef.name!!
                val serializedNameAnnotation = paramDef.findAnnotation<JsonName>()

                // Check if this parameter is the one for this JSON property.
                if (serializedNameAnnotation == null || serializedNameAnnotation.name != unmappedParamName) {
                    if (paramDefName != mappedParamName) {
                        return@forEach
                    }
                }

                // This is the right class parameter, populate it with the appropriate value.
                val param = when(paramDef.type.jvmErasure) {
                    Int::class -> mapInt(it.value)
                    Long::class -> mapLong(it.value)
                    Float::class -> mapFloat(it.value)
                    Double::class -> mapDouble(it.value)
                    Boolean::class -> mapBoolean(it.value)
                    String::class -> mapString(it.value)
                    List::class -> mapList(it.value, paramDef.type.arguments[0].type!!.jvmErasure)
                    else -> mapObject(it.value, paramDef.type.jvmErasure)
                }

                if (paramDef.type.isMarkedNullable || param != null) {
                    paramMap[paramDef] = param
                }
            }
        }

        cons.parameters.forEach { paramDef ->
            if (!paramMap.containsKey(paramDef)) {
                if (paramDef.type.isMarkedNullable) {
                    paramMap[paramDef] = null
                }
            }
        }

        return cons.callBy(paramMap)
    }

    private fun <T: Any> mapList(jsonValue: JsonValue, containedCls: KClass<T>): List<T> {
        val jsonList = jsonValue.value as List<JsonValue>
        val outList = ArrayList<T>()
        jsonList.forEach{
            outList.add(map(it, containedCls))
        }
        return outList
    }

    private fun mapInt(jsonValue: JsonValue): Int? {
        return jsonValue.value as Int?
    }

    private fun mapLong(jsonValue: JsonValue): Long? {
        return when (jsonValue.value) {
            is Int -> jsonValue.value.toLong()
            else -> jsonValue.value as Long?
        }
    }

    private fun mapFloat(jsonValue: JsonValue): Float? {
        return when (jsonValue.value) {
            is Int -> jsonValue.value.toFloat()
            is Long -> jsonValue.value.toFloat()
            else -> jsonValue.value as Float?
        }
    }

    private fun mapDouble(jsonValue: JsonValue): Double? {
        return when (jsonValue.value) {
            is Int -> jsonValue.value.toDouble()
            is Long -> jsonValue.value.toDouble()
            is Float -> jsonValue.value.toDouble()
            else -> jsonValue.value as Double?
        }
    }

    private fun mapBoolean(jsonValue: JsonValue): Boolean? {
        return jsonValue.value as Boolean?
    }

    private fun mapString(jsonValue: JsonValue): String? {
        return jsonValue.value as String?
    }
}
