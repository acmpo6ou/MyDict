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
     * Collects completions from all dictionaries.
     */
    fun search(text: String) {
        searchText.value = text

        val suggestions = mutableSetOf<String>()
        for (dict in dictsViewModel.dicts.value!!) {
            val words = dict.readArticlesPredictive(text).map { it.word }
            suggestions.addAll(words)
        }
        completions.value = suggestions.toList().sorted()
    }

    fun clearSearch() {
        searchText.value = ""
    }
}