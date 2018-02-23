package com.grundleborg.kson

class JsonParsingException(
        val type: String,
        val expected: String,
        val received: String
): Exception(
        "Unexpected characters found in JSON input for field with type `${type}`."+
        "Expected: `${expected}. Found: `${received}.")