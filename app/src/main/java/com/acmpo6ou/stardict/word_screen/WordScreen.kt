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

package com.acmpo6ou.stardict.word_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.BackButton
import com.acmpo6ou.stardict.NavIDs
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import dev.wirespec.jetmagic.navigation.navman

data class WordParams(
    val word: String,
    val transcription: String,
    val articles: Map<String, String>,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(p: WordParams) {
    Scaffold(topBar = { WordAppBar() }) {
        Column(modifier = Modifier.padding(it)) {
            WordRow(p.word, p.transcription)
            for (article in p.articles)
                Article(article.key, article.value)
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
        WordScreen(params)
    }
}

@Composable
fun WordRow(word: String, transcription: String) {
    Row {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(painterResource(R.drawable.ic_listen), "")
        }

        Column {
            Text(word, fontWeight = FontWeight.Bold)
            Text(transcription)
        }
    }
}

@Composable
fun Article(vocab: String, article: String) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            vocab, fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
        )

        // remove word and transcription from article
        val regex1 = Regex("<k>(.*?)</k>")
        val regex2 = Regex("<tr>(.*?)</tr>")
        var text = article.replace(regex1, "")
        text = text.replace(regex2, "")

        // TODO: render selectable html
        Text(text, fontSize = 25.sp)
    }
}

@Composable
fun WordAppBar() {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    SmallTopAppBar(
        title = {},
        navigationIcon = { BackButton() },
        actions = {
            IconButton(
                enabled = true,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    Icons.Default.Settings,
                    tint = Color.White,
                    contentDescription = "settings",
                )
            }

            IconButton(
                enabled = true,
                onClick = { navman.goto(composableResId = NavIDs.AboutScreen) }
            ) {
                Icon(
                    Icons.Default.Info,
                    tint = Color.White,
                    contentDescription = "about",
                )
            }
        },
        colors = colors,
    )
}
