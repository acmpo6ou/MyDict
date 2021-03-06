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

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.MainActivity
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.acmpo6ou.stardict.ui.theme.Yellow
import com.acmpo6ou.stardict.utils.AppBar
import com.acmpo6ou.stardict.utils.checkVolume
import com.acmpo6ou.stardict.utils.formatArticle
import com.ireward.htmlcompose.HtmlText
import java.io.File
import java.util.*

data class WordParams(
    val word: String,
    val transcription: String,
    val articles: Map<String, String>,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun WordScreen(
    p: WordParams,
    activity: MainActivity,
    settingsModel: SettingsViewModel,
    favoritesModel: FavoritesViewModel,
) {
    val fontSize = settingsModel.fontSize.value!!
    val fontPath = settingsModel.fontPath.value
    var fontFamily: FontFamily = FontFamily.Default

    fontPath?.let {
        fontFamily = FontFamily(Font(File(it)))
    }

    Scaffold(topBar = { AppBar(activity, "") }) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            WordRow(
                p.word, p.transcription,
                activity, favoritesModel, fontFamily,
            )
            
            val iter = p.articles.iterator()
            for (article in iter) {
                Article(article.key, article.value, fontSize, fontFamily)
                if (iter.hasNext()) Divider()
            }
        }
    }
}

@Composable
@Preview
fun WordScreenPreview() {
    val params = WordParams(
        "apple", "[trans]",
        mapOf("Universal" to "Article...")
    )
    StarDictTheme {
        WordScreen(
            params, MainActivity(),
            SettingsViewModel(), FavoritesViewModel(),
        )
    }
}

@Composable
fun WordRow(
    word: String,
    transcription: String,
    activity: MainActivity,
    favoritesModel: FavoritesViewModel,
    fontFamily: FontFamily,
) {
    val favorites: List<String> by favoritesModel.favorites.observeAsState(listOf())

    Row(modifier = Modifier.padding(8.dp)) {
        IconButton(
            onClick = {
                checkVolume(activity)
                activity.tts.language = Locale.US
                activity.tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, "")
            },
            modifier = Modifier.padding(8.dp),
        ) {
            Icon(
                painterResource(R.drawable.ic_listen),
                modifier = Modifier.size(50.dp), contentDescription = "play"
            )
        }

        Column {
            Text(word, fontWeight = FontWeight.Bold, fontFamily = fontFamily)
            Text(transcription, fontFamily = fontFamily)
        }
        Spacer(modifier = Modifier.weight(1f))

        if (word in favorites) {
            IconButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    val list = favoritesModel.favorites.value!!.toMutableList()
                    list.remove(word)
                    favoritesModel.favorites.value = list.toList()
                    favoritesModel.saveFavorites()
                },
            ) {
                Icon(
                    Icons.Rounded.Star,
                    modifier = Modifier.size(50.dp),
                    tint = Yellow,
                    contentDescription = "remove from favorites",
                )
            }
        } else {
            IconButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    val list = favoritesModel.favorites.value!!.toMutableList()
                    list.add(word)
                    favoritesModel.favorites.value = list.toList()
                    favoritesModel.saveFavorites()
                },
            ) {
                Icon(
                    painterResource(R.drawable.ic_empty_star),
                    modifier = Modifier.size(50.dp),
                    contentDescription = "add to favorites",
                )
            }
        }
    }
}

@Composable
fun Article(
    vocab: String,
    article: String,
    fontSize: Float,
    fontFamily: FontFamily,
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            vocab, fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            fontFamily = fontFamily,
        )

        SelectionContainer {
            HtmlText(
                formatArticle(article),
                style = TextStyle(
                    color = Color.White,
                    fontSize = fontSize.sp,
                    fontFamily = fontFamily,
                ),
            )
        }
    }
}
