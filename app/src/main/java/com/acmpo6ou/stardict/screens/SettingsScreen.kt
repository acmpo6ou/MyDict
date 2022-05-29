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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.acmpo6ou.stardict.utils.BackButton

class SettingsViewModel : ViewModel() {
    val fontSize = MutableLiveData(30)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(model: SettingsViewModel) {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    Scaffold(
        topBar = {
            SmallTopAppBar(
                { Text("Settings") }, colors = colors,
                navigationIcon = { BackButton() },
            )
        }
    ) {
        val fontSize = model.fontSize.observeAsState(30)
        OutlinedTextField(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            value = fontSize.value.toString(),
            label = { Text("Font size") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                model.fontSize.value = it.toInt()
            }
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    StarDictTheme {
        SettingsScreen(SettingsViewModel())
    }
}