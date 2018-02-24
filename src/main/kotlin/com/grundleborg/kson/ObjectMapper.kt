package com.grundleborg.kson

import java.io.Reader
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import java.lang.reflect.Type


class ObjectMapper {
    val typeMap: MutableMap<Type, KClass<*>> = mutableMapOf()

    /**
     * Parse a JSON string and map it to the
     */
    inline fun <reified T: Any> parse(json: String): T {
        return parse(json, T::class)
    }

    inline fun <reified T: Any> parse(json: Reader): T {
        return parse(json, T::class)
    }

    fun <T: Any> parse(json: String, cls: KClass<T>): T {
        val parser = JsonParser(json)
        return map(parser.parse(), cls)
    }

    fun <T: Any> parse(json: Reader, cls: KClass<T>): T {
        val builder = StringBuilder()
        var charsRead = -1
        val chars = CharArray(100)
        do {
            charsRead = json.read(chars, 0, chars.size)
            if (charsRead > 0)
                builder.append(chars, 0, charsRead)
        } while (charsRead > 0)
        val stringReadFromReader = builder.toString()
        return parse(stringReadFromReader, cls)
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

        cons.parameters.forEachIndexed { idx, paramDef ->
            if (jsonObject.containsKey(paramDef.name)) {
                val value = jsonObject.getValue(paramDef.name!!)

                val type = paramDef.type

                val param = when(paramDef.type.jvmErasure) {
                    Int::class -> mapInt(value)
                    Long::class -> mapLong(value)
                    Float::class -> mapFloat(value)
                    Double::class -> mapDouble(value)
                    Boolean::class -> mapBoolean(value)
                    String::class -> mapString(value)
                    List::class -> mapList(value, paramDef.type.arguments[0].type!!.jvmErasure)
                    else -> mapObject(value, paramDef.type.jvmErasure)
                }

                if (paramDef.type.isMarkedNullable || param != null) {
                    paramMap[paramDef] = param
                }
            } else if (paramDef.type.isMarkedNullable) {
                paramMap[paramDef] = null
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
        return jsonValue.value as Long?
    }

    private fun mapFloat(jsonValue: JsonValue): Float? {
        return jsonValue.value as Float?
    }

    private fun mapDouble(jsonValue: JsonValue): Double? {
        return jsonValue.value as Double?
    }

    private fun mapBoolean(jsonValue: JsonValue): Boolean? {
        return jsonValue.value as Boolean?
    }

    private fun mapString(jsonValue: JsonValue): String? {
        return jsonValue.value as String?
    }
}
