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

package com.grundleborg.kson.namemapper

import com.grundleborg.kson.SimpleNameMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SimpleNameMapperTests {
    @Test
    fun `to json`() {
        val inputs = listOf(
                "word",
                "some_words",
                "lotsOfWords",
                "capitalsEVERYWHERE"
        )

        val nm = SimpleNameMapper()

        inputs.forEachIndexed { index, input ->
            assertThat(nm.toJson(input)).isEqualTo(inputs[index])
        }
    }

    @Test
    fun `from json`() {
        val inputs = listOf(
                "word",
                "some_words",
                "lotsOfWords",
                "capitalsEVERYWHERE"
        )

        val nm = SimpleNameMapper()

        inputs.forEachIndexed { index, input ->
            assertThat(nm.fromJson(input)).isEqualTo(inputs[index])
        }
    }
}