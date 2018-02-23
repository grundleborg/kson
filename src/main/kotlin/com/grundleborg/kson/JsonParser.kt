package com.grundleborg.kson

class JsonParser(data: String) {
    val data = data
    var index = -1

    private fun skipWhitespace() {
        while (true) {
            when (peek()) {
                ' ', '\n', '\r', '\t' -> next()
                else -> return
            }
        }
    }

    fun parse(): JsonValue {
        skipWhitespace()
        return value()
    }

    fun next(): Char {
        index += 1
        return data[index]
    }

    fun next(n: Int): String {
        val sub = data.substring(index+1, index+1+n)
        index += n
        return sub
    }

    fun peek(): Char {
        return data[index+1]
    }

    fun peek(n: Int): String {
        return data.substring(index+1, index+1+n)
    }

    fun value(): JsonValue {
        skipWhitespace()
        val nextChar = peek()
        return when(nextChar) {
            '{' -> parseObject()
            '[' -> parseArray()
            '"' -> parseString()
            't' -> parseTrue()
            'f' -> parseFalse()
            'n' -> parseNull()
            '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> parseNumber()
            else -> throw Exception("Unrecognised value: ${nextChar}")
        }
    }

    private fun parseObject(): JsonValue {
        val jsonObject: HashMap<String, JsonValue> = HashMap()

        if (next() != '{') {
            throw Exception("Object must start with { character.")
        }

        while(true) {
            skipWhitespace()

            if (peek() == '}') {
                next()
                break
            }

            val jsonKey = parseString()

            skipWhitespace()

            if (peek() != ':') {
                throw Exception("Expected value after Object key. Got: "+peek())
            }

            next()

            val jsonValue = value()

            skipWhitespace()

            if (peek() != ',' && peek() != '}') {
                throw Exception("Not a comma or a end object.")
            }
            if (peek() == ',') {
                next()
            }

            jsonObject.put(jsonKey.value as String, jsonValue)
        }

        return JsonValue(jsonObject)
    }

    private fun parseArray(): JsonValue {
        val jsonArray: ArrayList<JsonValue> = ArrayList()

        if (next() != '[') {
            throw Exception("Array must start with [ character.")
        }

        while (true) {
            skipWhitespace()

            if (peek() == ']') {
                next()
                break
            }

            if (peek() == ',') {
                next()
                continue
            }

            val jsonValue = value()
            jsonArray.add(jsonValue)
        }

        return JsonValue(jsonArray)
    }

    private fun parseString(): JsonValue {
        // TODO: Handle unicode escapes (\uXXXX)

        var string: String = ""

        if (next() != '"') {
            throw Exception("String must start with \" character.")
        }

        while (true) {
            val character = next()
            if (character == '\\') {
                val escapedCharacter = next()
                string += when (escapedCharacter) {
                    'b' -> '\b'
                    'n' -> '\n'
                    't' -> '\t'
                    'r' -> '\r'
                    'f' -> 0x0C.toChar()
                    '"' -> '\"'
                    '\\' -> '\\'
                    else -> throw Exception("Unrecognised escape sequence: `\\${escapedCharacter}`.")
                }
            } else if (character == '"') {
                break
            } else {
                string += character
            }
        }

        return JsonValue(string)
    }

    private fun parseTrue(): JsonValue {
        val chars = next(4)
        return when (chars) {
            "true" -> JsonValue(true)
            else -> throw JsonFieldParsingException("Boolean", "true", chars)
        }
    }

    private fun parseFalse(): JsonValue {
        val chars = next(5)
        return when (chars) {
            "false" -> JsonValue(false)
            else -> throw JsonFieldParsingException("Boolean", "false", chars)
        }
    }

    private fun parseNull(): JsonValue {
        val chars = next(4)
        return when (chars) {
            "null" -> JsonValue(null)
            else -> throw JsonFieldParsingException("null", "null", chars)
        }
    }

    private fun parseNumber(): JsonValue {
        // TODO: Handle Exponential Form
        // TODO: Handle Decimals
        // TODO: Restrict minus sign to start of string.

        var accumulator: String = ""
        while (true) {
            if (peek() in "-1234567890") {
                accumulator += next()
            } else {
                break;
            }
        }

        return JsonValue(accumulator.toInt())
    }
}