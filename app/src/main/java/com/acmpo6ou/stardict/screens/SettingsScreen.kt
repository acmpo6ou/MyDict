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

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acmpo6ou.stardict.MainActivity
import com.acmpo6ou.stardict.ui.theme.DarkGrey
import com.acmpo6ou.stardict.ui.theme.Green
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.acmpo6ou.stardict.utils.AppBar
import com.acmpo6ou.stardict.utils.ColorView
import com.acmpo6ou.stardict.utils.FontPicker
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.ARGBPickerState
import com.vanpra.composematerialdialogs.color.ColorPalette
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.io.File

class SettingsViewModel : ViewModel() {
    lateinit var prefs: SharedPreferences
    lateinit var fonts: Map<File, Font>

    val fontSize = MutableLiveData(30f)
    val fontPath = MutableLiveData<String>()
    val primaryColor = MutableLiveData(Green)

    fun loadPrefs() {
        fontSize.value = prefs.getFloat("font_size", 30f)
        fontPath.value = prefs.getString("font_path", null)
        val color = prefs.getLong("primary_color", Green.value.toLong())
        primaryColor.value = Color(color.toULong())
    }

    fun savePrefs() {
        prefs.edit()
            .putFloat("font_size", fontSize.value!!)
            .putLong("primary_color", primaryColor.value!!.value.toLong())
            .putString("font_path", fontPath.value)
            .apply()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(model: SettingsViewModel, activity: MainActivity) {
    Scaffold(topBar = { AppBar(activity, "Settings") }) {
        val fontSize = model.fontSize.observeAsState(30)

        Column {
            OutlinedTextField(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = fontSize.value.toInt().toString(),
                label = { Text("Font size") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { value ->
                    if (value.isNotEmpty()) {
                        model.fontSize.value = value.toFloat()
                        model.savePrefs()
                    }
                }
            )

            val colorDialog = rememberMaterialDialogState()
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Primary color:", fontSize = 25.sp)
                Spacer(modifier = Modifier.weight(1f))
                ColorView(model.primaryColor) { colorDialog.show() }
            }

            MaterialDialog(
                colorDialog, backgroundColor = DarkGrey,
                buttons = {
                    positiveButton(
                        "OK",
                        textStyle = TextStyle(MaterialTheme.colorScheme.primary),
                    )
                    negativeButton(
                        "CANCEL",
                        textStyle = TextStyle(MaterialTheme.colorScheme.primary),
                    )
                }
            ) {
                colorChooser(
                    colors = ColorPalette.Primary,
                    argbPickerState = ARGBPickerState.WithoutAlphaSelector,
                    onColorSelected = {
                        model.primaryColor.value = it
                        model.savePrefs()
                    }
                )
            }

            FontPicker(model)
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    StarDictTheme {
        SettingsScreen(SettingsViewModel(), MainActivity())
    }
}
