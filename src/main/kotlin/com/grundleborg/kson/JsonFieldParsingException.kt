package com.grundleborg.kson

class JsonFieldParsingException(
        type: String,
        expected: String,
        received: String
): Exception(
        "Unexpected characters found in JSON input for field with type `$type`."+
        "Expected: `$expected. Found: `$received.")