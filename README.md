KSON
====

A simple, highly opinionated, entirely unoptimised JSON decoder for Kotlin.

Features
--------

* Implemented entirely in Kotlin, and intended for 100% Kotlin projects.
* JSON spec compliant parser.
* Object mapper for mapping into custom Kotlin data classes.
* Supports Kotlin default parameters for missing and null JSON properties.

Limitations & Known Issues
--------------------------

* The built-in JSON parser is very simple entirely unoptimised, so don't expect amazing performance.
* Object Mapper only supports object instantiation through the default constructor.
* No JSON *encoding* support.
* Object Mapper cannot process top-level JSON arrays.
* Object Mapper makes heavy use of reflection, without any kind of caching, so, once again, don't expect amazing performance. 


Installation
------------

Add the following line to the `dependencies` section of your `build.gradle`:

```groovy
compile 'com.grundleborg.kson:kson:0.0.1'
```


Example
-------

```kotlin
data class Item(
        val name: String,
        val number: Int
)

data class ApiResult(
        val title: String,
        val items: List<out Item>,
        val otherStuff: String = "nothing to see here"
)

fun jsonDecode() {
    val json = """{"title": "Title", "items": [{"name": "Item 1", "number": 1},{"name": "Item 2", "number": 2}]}"""
    val om = ObjectMapper()
    val result = om.parse(json)

    println("Title: ${result.title}.")
    println("Other Stuff: ${result.otherStuff}.")
    result.items.foreach{ println("Item Name: ${it.name},Nnumber: ${it.number}.")}
}
```

Contributing
------------

* If you find a bug or missing feature, please file a ticket on the KSON Github issue tracker.
* Contributions in the form of Pull Requests are also welcome, although please open an issue to discuss first if you are proposing a major change.

License
-------

```
Copyright 2018 George Goldberg

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```