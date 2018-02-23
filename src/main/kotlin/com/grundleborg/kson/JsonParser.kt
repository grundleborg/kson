package com.grundleborg.kson

class JsonParser(data: String) {
    val data = data
    var index = -1

    fun skipWhitespace() {
        while (true) {
            when (peek()) {
                ' ', '\n', '\r', '\t' -> next()
                else -> return
            }
        }
    }

    fun parse(): JsonValue {
        skipWhitespace()
        return value(next())
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

    fun value(character: Char): JsonValue {
        return when(character) {
            '{' -> parseObject()
            '[' -> parseArray()
            '"' -> parseString()
            't', 'f' -> parseBool(character)
            'n' -> parseNull()
            '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> parseNumber(character)
            else -> throw Exception("Unrecognised value.")
        }
    }

    fun parseObject(): JsonValue {
        val jsonObject: HashMap<String, JsonValue> = HashMap()

        while(true) {
            skipWhitespace()

            if (peek() == '}') {
                next()
                break
            }

            next() // Should eat the opening quotes of the key.

            val jsonKey = parseString()

            skipWhitespace()

            if (peek() != ':') {
                throw Exception("Expected value after Object key. Got: "+peek())
            }

            next()

            skipWhitespace()

            val character = next()
            val jsonValue = value(character)

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

    fun parseArray(): JsonValue {
        val jsonArray: ArrayList<JsonValue> = ArrayList()

        while (true) {
            skipWhitespace()

            if (peek() == ']') {
                next()
                break
            }

            skipWhitespace()

            val character = next()

            if (character == ',') {
                continue
            }

            val jsonValue = value(character)
            jsonArray.add(jsonValue)
        }

        return JsonValue(jsonArray)
    }

    fun parseString(): JsonValue {
        // TODO: Handle escapes, including unicode.

        var string: String = ""

        while (true) {
            val character = next()
            if (character == '"') {
                break
            }
            string += character
        }

        return JsonValue(string)
    }

    fun parseBool(character: Char): JsonValue {
        if (character == 't' && next(3) == "rue") {
            return JsonValue(true)
        } else if (character == 'f' && next(4) == "alse") {
            return JsonValue(false)
        } else {
            throw Exception("Unexpected characters in boolean value.")
        }
    }

    fun parseNull(): JsonValue {
        if (next(3) != "ull") {
            throw Exception("Unexpected characters in null value.")
        }
        return JsonValue(null)
    }

    fun parseNumber(character: Char): JsonValue {
        // TODO: Handle Exponential Form
        // TODO: Handle Decimals

        var accumulator: String = ""
        accumulator += character
        while (true) {
            if (peek() in "1234567890") {
                accumulator += next()
            } else {
                break;
            }
        }

        return JsonValue(accumulator.toInt())
    }
}