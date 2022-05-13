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
import com.acmpo6ou.stardict.AppBar
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import io.github.eb4j.stardict.StarDictDictionary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO: should except a list of Words from different vocabs
fun WordScreen(word: StarDictDictionary.Entry) {
    Scaffold(topBar = { AppBar() }) {
        Column(modifier = Modifier.padding(it)) {
            WordRow(word.word, "[trans]")
        }
    }
}

@Composable
@Preview
fun WordScreenPreview() {
    StarDictTheme {
        val word = StarDictDictionary.Entry(
            "apple",
            StarDictDictionary.EntryType.HTML,
            "Article.",
        )
        WordScreen(word)
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
