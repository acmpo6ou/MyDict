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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.BackButton
import com.acmpo6ou.stardict.BuildConfig
import com.acmpo6ou.stardict.NavIDs.AboutScreen
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import dev.wirespec.jetmagic.composables.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    Scaffold(
        topBar = {
            SmallTopAppBar(
                { Text("About") }, colors = colors,
                navigationIcon = { BackButton() },
            )
        }
    ) {
        AboutContent(it)
    }
}

@Composable
fun AboutContent(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(R.drawable.icon))
            Column {
                Text("Author: Bohdan Kolvakh")
                Text("Version: ${BuildConfig.VERSION_NAME}")
            }
        }

        val uriHandler = LocalUriHandler.current
        val licenseString = buildAnnotatedString {
            append("License: ")
            pushStringAnnotation(
                tag = "GPLv3",
                annotation = "https://www.gnu.org/licenses/gpl-3.0.en.html",
            )
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("GPLv3")
            }
            pop()
        }
        ClickableText(
            text = licenseString,
            onClick = {
                val annotation = licenseString
                    .getStringAnnotations("GPLv3", it, it).first()
                uriHandler.openUri(annotation.item)
            },
            style = TextStyle(fontSize = 22.sp, color = Color.White)
        )

        val sourceCodeString = buildAnnotatedString {
            append("Source Code: ")
            pushStringAnnotation(
                tag = "code",
                annotation = "https://github.com/acmpo6ou/StarDict",
            )
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("GitHub")
            }
            pop()
        }
        ClickableText(
            text = sourceCodeString,
            onClick = {
                val annotation = sourceCodeString
                    .getStringAnnotations("code", it, it).first()
                uriHandler.openUri(annotation.item)
            },
            style = TextStyle(fontSize = 22.sp, color = Color.White)
        )
    }
}

@Composable
@Preview
fun AboutScreenPreview() {
    StarDictTheme {
        AboutScreen()
    }
}
