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

import java.io.Reader

class JsonParser(data: Reader) {
    private val data = data
    private var nextChar = data.read().toChar()

    /**
     * Run the parser with the data it was initialised with.
     *
     * @return a JsonValue representing the root element of the JSON data provided.
     */
    fun parse(): JsonValue {
        skipWhitespace()
        return parseValue()
    }

    private fun next(): Char {
        nextChar = data.read().toChar()
        return nextChar
    }

    private fun next(n: Int): String {
        val sb = StringBuilder()
        for (i in 1..n) {
            nextChar = data.read().toChar()
            sb.append(nextChar)
        }
        return sb.toString()
    }

    private fun skipWhitespace() {
        while (true) {
            when (nextChar) {
                ' ', '\n', '\r', '\t' -> next()
                else -> return
            }
        }
    }

    private fun parseValue(): JsonValue {
        return when(nextChar) {
            '{' -> parseObject()
            '[' -> parseArray()
            '"' -> parseString()
            't' -> parseTrue()
            'f' -> parseFalse()
            'n' -> parseNull()
            '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> parseNumber()
            else -> throw Exception("Unrecognised parseValue: ${nextChar}")
        }
    }

    private fun parseObject(): JsonValue {
        val jsonObject: HashMap<String, JsonValue> = HashMap()

        while(true) {
            next()
            skipWhitespace()

            if (nextChar == '}') {
                break
            }

            val jsonKey = parseString()

            skipWhitespace()

            if (nextChar != ':') {
                throw Exception("Expected parseValue after Object key. Got: "+nextChar)
            }

            next()
            skipWhitespace()

            val jsonValue = parseValue()

            skipWhitespace()

            if (nextChar != ',' && nextChar != '}') {
                throw Exception("Not a comma or a end object.")
            }

            jsonObject.put(jsonKey.value as String, jsonValue)

            if (nextChar == '}') {
                next()
                break
            }
        }

        return JsonValue(jsonObject)
    }

    private fun parseArray(): JsonValue {
        // FIXME: Use Kotlin style list instantiation.
        val jsonArray: ArrayList<JsonValue> = ArrayList()

        if (nextChar != '[') {
            throw Exception("Array must start with [ character.")
        }

        next()

        while (true) {
            skipWhitespace()

            if (nextChar == ']') {
                break
            }

            val jsonValue = parseValue()
            jsonArray.add(jsonValue)

            skipWhitespace()

            if (nextChar == ',') {
                next()
            } else if (nextChar == ']') {
                break
            }
        }

        next()

        return JsonValue(jsonArray)
    }

    private fun parseString(): JsonValue {
        val builder = StringBuilder()

        if (nextChar != '"') {
            throw Exception("String must start with \" character.")
        }

        while (true) {
            next()
            if (nextChar == '\\') {
                val escapedCharacter = next()
                builder.append(when (escapedCharacter) {
                    'b' -> '\b'
                    'n' -> '\n'
                    't' -> '\t'
                    'r' -> '\r'
                    'f' -> 0x0C.toChar()
                    '"' -> '\"'
                    '\\' -> '\\'
                    'u' -> {
                        val unicode = next(4)
                        unicode.toInt(radix=16).toChar()
                    }
                    else -> throw Exception("Unrecognised escape sequence: `\\$escapedCharacter`.")
                })
            } else if (nextChar == '"') {
                next()
                break
            } else {
                builder.append(nextChar)
            }
        }

        return JsonValue(builder.toString())
    }

    private fun parseTrue(): JsonValue {
        val chars = next(3)
        next()
        return when (chars) {
            "rue" -> JsonValue(true)
            else -> throw JsonFieldParsingException("Boolean", "true", chars)
        }
    }

    private fun parseFalse(): JsonValue {
        val chars = next(4)
        next()
        return when (chars) {
            "alse" -> JsonValue(false)
            else -> throw JsonFieldParsingException("Boolean", "false", chars)
        }
    }

    private fun parseNull(): JsonValue {
        val chars = next(3)
        next()
        return when (chars) {
            "ull" -> JsonValue(null)
            else -> throw JsonFieldParsingException("null", "null", chars)
        }
    }

    private fun parseNumber(): JsonValue {
        val builder = StringBuilder()
        var isExponential = false

        while (true) {
            if (nextChar in "-1234567890") {
                builder.append(nextChar)
            } else if (nextChar in "eE.") {
                builder.append(nextChar)
                isExponential = true
            } else {
                break;
            }

            next()
        }

        var result: JsonValue
        if (isExponential) {
            val floatResult = builder.toString().toFloat()
            if (floatResult == Float.POSITIVE_INFINITY || floatResult == Float.NEGATIVE_INFINITY) {
                result = JsonValue(builder.toString().toDouble())
            } else {
                result = JsonValue(floatResult)
            }
        } else {
            try {
                result = JsonValue(builder.toString().toInt())
            } catch(e: NumberFormatException) {
                result = JsonValue(builder.toString().toLong())
            }
        }

        return result
    }
}