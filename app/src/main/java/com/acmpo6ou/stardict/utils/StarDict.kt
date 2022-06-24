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

package com.acmpo6ou.stardict.utils

import java.io.File
import java.util.stream.Collectors.toMap
import org.yage.dict.star.StarDictParser
import org.yage.dict.star.WordPosition

class StarDict {
    lateinit var name: String
    lateinit var filePath: String

    private val parser = StarDictParser()
    private lateinit var positions: Map<String, WordPosition>

    fun initialize(filePath: String) {
        this.filePath = filePath
        loadIfoFile("$filePath.ifo")
        parser.loadIndexFile("$filePath.idx")
        loadContentFile(filePath)
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
     * Loads .dict or .dict.dz content file.
     */
    fun loadContentFile(path: String) {
        val file = File("$path.dict.dz")
        if (file.exists()) parser.loadContentFile("$path.dict.dz")
        else parser.loadContentFile("$path.dict")
    }

    /**
     * Returns a list of completion suggestions for a given [text].
     */
    fun getSuggestions(text: String): List<String> {
        positions =
            parser.searchWord(text).stream().collect(
            toMap(
                Map.Entry<String, WordPosition>::key,
                Map.Entry<String, WordPosition>::value,
            )
        ) as Map<String, WordPosition>
        return positions.map { it.key }
    }

    /**
     * Returns an article for a given [word] or null if the word wasn't found.
     */
    fun getArticle(word: String): String? {
        val position = positions[word] ?: return null
        return parser.getWordExplanation(position.startPos, position.length)
    }
}
