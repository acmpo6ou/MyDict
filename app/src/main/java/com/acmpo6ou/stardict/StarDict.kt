/*
 * StarDict implementation for Android.
 * Copyright (C) 2022  Bohdan Kolvakh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.acmpo6ou.stardict

import org.yage.dict.star.StarDictParser
import java.io.File

class StarDict {
    lateinit var name: String
    lateinit var filePath: String
    private val parser = StarDictParser()

    fun initialize(filePath: String) {
        this.filePath = filePath
        loadIfoFile("$filePath.ifo")
        parser.loadIndexFile("$filePath.idx")
        parser.loadContentFile("$filePath.dict")
    }

    /**
     * Loads dict info (such as dict name) from given .ifo file.
     */
    private fun loadIfoFile(path: String) {
        val file = File(path)
        for (line in file.readLines())
            if (line.startsWith("bookname=")) {
                name = line.removePrefix("bookname=")
                break
            }
    }

    /**
     * Returns a list of completion suggestions for a given [text].
     */
    fun getSuggestions(text: String): List<String> =
        parser.searchWord(text).map { it.key }

    /**
     * Returns an article for a given [word] or null if the word wasn't found.
     */
    fun getArticle(word: String): String? {
        val articles = parser.searchWord(word)
        if (articles.isEmpty()) return null

        val position = articles.first().value
        return parser.getWordExplanation(position.startPos, position.length)
    }
}
