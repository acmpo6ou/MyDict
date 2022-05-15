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
