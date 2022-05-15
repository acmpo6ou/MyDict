package com.acmpo6ou.stardict.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import dev.wirespec.jetmagic.composables.Image

@Composable
fun AboutScreen() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(R.drawable.icon))
        Column {
            Text("Author: Bohdan Kolvakh")
            Text("Version: 1.0.0")
        }
    }
}

@Composable
@Preview
fun AboutScreenPreview() {
    StarDictTheme {
        AboutScreen()
    }
}
