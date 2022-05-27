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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.acmpo6ou.stardict.MainActivity
import com.acmpo6ou.stardict.NavIDs
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.acmpo6ou.stardict.utils.HtmlText
import com.acmpo6ou.stardict.utils.checkVolume
import com.acmpo6ou.stardict.utils.formatArticle
import dev.wirespec.jetmagic.navigation.navman
import java.util.*

data class WordParams(
    val word: String,
    val transcription: String,
    val articles: Map<String, String>,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(p: WordParams, activity: MainActivity) {
    Scaffold(topBar = { WordAppBar() }) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            WordRow(p.word, p.transcription, activity)
            val iter = p.articles.iterator()
            for (article in iter) {
                Article(article.key, article.value)
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
        WordScreen(params, MainActivity())
    }
}

@Composable
fun WordRow(word: String, transcription: String, activity: MainActivity) {
    Row(modifier = Modifier.padding(8.dp)) {
        IconButton(
            onClick = {
                checkVolume(activity)

                var tts: TextToSpeech? = null
                tts = TextToSpeech(activity) {
                    tts?.language = Locale.US
                    tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, "")
                }
            },
            modifier = Modifier.padding(8.dp),
        ) {
            Icon(
                painterResource(R.drawable.ic_listen),
                modifier = Modifier.size(50.dp), contentDescription = "play"
            )
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
        HtmlText(formatArticle(article))
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
