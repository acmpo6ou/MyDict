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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acmpo6ou.stardict.dicts_screen.DictsViewModel

class MainViewModel : ViewModel() {
    lateinit var dictsViewModel: DictsViewModel

    val searchText = MutableLiveData("")
    val completions = MutableLiveData(listOf<String>())

    /**
     * Searches for [text] in all dictionaries.
     *
     * Collects completions from all dictionaries, only first 40 completions
     * will be used.
     */
    fun search(text: String) {
        val text = text.trim()
        if (text.isEmpty()) {
            completions.value = listOf()
            return
        }

        val suggestions = mutableSetOf<String>()
        for (dict in dictsViewModel.dicts.value!!) {
            val words = dict.getSuggestions(text)
            suggestions.addAll(words)
        }
        completions.value = suggestions
            .toList()
            .sortedBy { it.length }
            .take(40)
    }

    /**
     * Collects articles for a given [word] from all dictionaries.
     */
    fun getArticles(word: String): Map<String, String> {
        val articles = mutableMapOf<String, String>()
        for (dict in dictsViewModel.dicts.value!!) {
            val article = dict.getArticle(word)
            article?.let { articles[dict.name] = article }
        }
        return articles
    }

    /**
     * Extracts [word] transcription from its articles, the transcription
     * is usually contained in the <tr> tag.
     */
    fun getTranscription(word: String): String {
        val articles = getArticles(word)
        val joined = articles.values.joinToString()
        val regex = Regex("<tr>(.*?)</tr>")
        val trans = regex.find(joined)?.value ?: ""
        return trans.removePrefix("<tr>").removeSuffix("</tr>")
    }
}
