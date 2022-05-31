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

package com.acmpo6ou.stardict.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acmpo6ou.stardict.MainActivity
import com.acmpo6ou.stardict.NavIDs
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.acmpo6ou.stardict.utils.AppBar
import dev.wirespec.jetmagic.navigation.navman

class FavoritesViewModel : ViewModel() {
    val favorites = MutableLiveData(listOf<String>())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(activity: MainActivity, model: FavoritesViewModel) {
    val favorites: List<String> by model.favorites.observeAsState(listOf())

    Scaffold(
        topBar = { AppBar(activity, "🌟Favorites🌟") }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(favorites.size) { i ->
                val word = favorites[i]
                Row(
                    modifier = Modifier.clickable {
                        activity.mainViewModel.search(word)
                        navman.goto(
                            composableResId = NavIDs.WordScreen,
                            p = WordParams(
                                word, activity.mainViewModel.getTranscription(word),
                                activity.mainViewModel.getArticles(word)
                            )
                        )
                        activity.mainViewModel.completions.value = listOf()
                    }
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(word, fontSize = 26.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    val model = FavoritesViewModel()
    model.favorites.value = listOf("apple", "communication", "testing")

    StarDictTheme {
        FavoritesScreen(MainActivity(), model)
    }
}
