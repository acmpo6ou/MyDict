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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.AppBar
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(
    word: String,
    transcription: String,
    articles: Map<String, String>,
) {
    Scaffold(topBar = {}) {
        Column(modifier = Modifier.padding(it)) {
            WordRow(word, transcription)
            // TODO: render articles
            Article(articles.keys.first(), articles.values.first())
        }
    }
}

@Composable
@Preview
fun WordScreenPreview() {
    StarDictTheme {
        WordScreen(
            "apple", "[trans]",
            mapOf("Universal" to "Article...")
        )
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
            fontSize = 25.sp,
        )
        // TODO: remove transcription from article
        // TODO: render selectable html
        Text(article, fontSize = 20.sp)
    }
}
