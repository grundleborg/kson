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

import com.grundleborg.kson.UnderscoreCamelCaseNameMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UnderscoreCamelCaseNameMapperTests {
    @Test
    fun `to json`() {
        val inputs = listOf(
                "single",
                "a",
                "simpleTest",
                "lotsOfWordsTest",
                "isHTMLValid",
                "StartsWithCapital",
                "B",
                "HTML",
                "HTMLFairy",
                "isAGoodOne",
                "hasAn_InIt"
        )

        val outputs = listOf(
                "single",
                "a",
                "simple_test",
                "lots_of_words_test",
                "is_html_valid",
                "starts_with_capital",
                "b",
                "html",
                "html_fairy",
                "is_a_good_one",
                "has_an__in_it"
        )

        val nm = UnderscoreCamelCaseNameMapper()

        inputs.forEachIndexed { index, input ->
            assertThat(nm.toJson(input)).isEqualTo(outputs[index])
        }
    }

    @Test
    fun `from json`() {
        val inputs = listOf(
                "single",
                "a",
                "simple_test",
                "lots_of_words_test",
                "conTains_a_Capital",
                "has__two_underscores",
                "acronym_like_HTML_example",
                "_starts_with_underscore"
        )

        val outputs = listOf(
                "single",
                "a",
                "simpleTest",
                "lotsOfWordsTest",
                "conTainsACapital",
                "hasTwoUnderscores",
                "acronymLikeHTMLExample",
                "startsWithUnderscore"
        )

        val nm = UnderscoreCamelCaseNameMapper()

        inputs.forEachIndexed { index, input ->
            assertThat(nm.fromJson(input)).isEqualTo(outputs[index])
        }
    }
}