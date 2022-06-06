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

package com.acmpo6ou.stardict.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.screens.SettingsViewModel
import com.acmpo6ou.stardict.ui.theme.DarkGrey
import java.io.File

@OptIn(ExperimentalTextApi::class)
@Composable
fun FontPicker(model: SettingsViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    val path by model.fontPath.observeAsState()

    Column(
        modifier = Modifier
            .clickable { showMenu = true }
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text("Font:")

        if (path == null) {
            Text("Default")
            return@Column
        }

        val fontFile = File(path)
        Text(
            fontFile.nameWithoutExtension,
            fontFamily = FontFamily(Font(fontFile)),
        )
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
        modifier = Modifier.background(DarkGrey),
    ) {

        DropdownMenuItem(
            text = { Text("Default", fontSize = 25.sp) },
            onClick = {
                model.fontPath.value = null
                model.savePrefs()
                showMenu = false
            }
        )

        for ((file, font) in model.fonts) {
            DropdownMenuItem(
                text = {
                    Text(
                        file.nameWithoutExtension,
                        fontFamily = FontFamily(font),
                        fontSize = 25.sp,
                    )
                },
                onClick = {
                    model.fontPath.value = file.path
                    model.savePrefs()
                    showMenu = false
                }
            )
        }
    }
}
