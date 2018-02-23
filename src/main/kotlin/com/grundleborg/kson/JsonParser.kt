package com.grundleborg.kson

class JsonParser(data: String) {
    val data = data
    var index = -1

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
        index += 1
        return data[index]
    }

    private fun next(n: Int): String {
        val sub = data.substring(index+1, index+1+n)
        index += n
        return sub
    }

    private fun peek(): Char {
        return data[index+1]
    }

    private fun peek(n: Int): String {
        return data.substring(index+1, index+1+n)
    }

    private fun skipWhitespace() {
        while (true) {
            when (peek()) {
                ' ', '\n', '\r', '\t' -> next()
                else -> return
            }
        }
    }

    private fun parseValue(): JsonValue {
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
            else -> throw Exception("Unrecognised parseValue: ${nextChar}")
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
                throw Exception("Expected parseValue after Object key. Got: "+peek())
            }

            next()

            val jsonValue = parseValue()

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

            val jsonValue = parseValue()
            jsonArray.add(jsonValue)
        }

        return JsonValue(jsonArray)
    }

    private fun parseString(): JsonValue {
        val builder = StringBuilder()

        if (next() != '"') {
            throw Exception("String must start with \" character.")
        }

        while (true) {
            val character = next()
            if (character == '\\') {
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
            } else if (character == '"') {
                break
            } else {
                builder.append(character)
            }
        }

        return JsonValue(builder.toString())
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
        val builder = StringBuilder()
        var isExponential = false

        while (true) {
            val character: Char

            try {
                character = peek()
            } catch(e: StringIndexOutOfBoundsException) {
                break;
            }

            if (character in "-1234567890") {
                builder.append(next())
            } else if (character in "eE.") {
                builder.append(next())
                isExponential = true
            } else {
                break;
            }
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