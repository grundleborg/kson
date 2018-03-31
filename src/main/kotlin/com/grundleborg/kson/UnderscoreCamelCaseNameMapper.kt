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

class UnderscoreCamelCaseNameMapper : NameMapper {

    override fun toJson(paramName: String): String {
        val sb = StringBuilder()
        var nextCharCanBeUnderscore = false

        paramName.forEachIndexed { index, it ->
            if (nextCharCanBeUnderscore && it.isUpperCase()) {
                sb.append('_')
                sb.append(it.toLowerCase())
                nextCharCanBeUnderscore = false
            } else if (it.isUpperCase()) {
                if (index > 0 && paramName.length > index + 1 && !paramName[index+1].isUpperCase()) {
                    sb.append('_')
                }
                sb.append(it.toLowerCase())
                nextCharCanBeUnderscore = false
            } else {
                sb.append(it);
                nextCharCanBeUnderscore = true
            }
        }

        return sb.toString();
    }

    override fun fromJson(keyName: String): String {
        val sb = StringBuilder()
        var fragmentCount = 0

        keyName.split("_").forEachIndexed { index, fragment ->
            if (fragment.isEmpty()) {
                return@forEachIndexed
            }

            sb.append(when(fragmentCount) {
                0 -> fragment
                else -> fragment[0].toUpperCase() + fragment.substring(1)
            })

            fragmentCount++
        }

        return sb.toString()
    }
}