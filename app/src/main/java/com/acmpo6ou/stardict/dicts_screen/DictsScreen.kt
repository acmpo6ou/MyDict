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

package com.acmpo6ou.stardict.dicts_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acmpo6ou.stardict.*
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import dev.wirespec.jetmagic.navigation.navman

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictsScreen(activity: MainActivity, model: DictsViewModel) {
    Scaffold(
        topBar = { DictsAppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { activity.importDictDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
            ) {
                Icon(Icons.Default.Add, "Import dict")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        RemoveDictDialog(model)
        DictsList(model, it)
    }
}

@Composable
@Preview
fun DictsScreenPreview() {
    val model = DictsViewModel()
    val dict = StarDict()
    val dict2 = StarDict()
    dict.name = "Computer"
    dict2.name = "Universal"
    model.dicts.value = listOf(dict, dict2)

    StarDictTheme {
        DictsScreen(MainActivity(), model)
    }
}

@Composable
@Preview
fun DictsScreenEmptyPreview() {
    StarDictTheme {
        DictsScreen(MainActivity(), DictsViewModel())
    }
}

@Composable
fun DictsAppBar() {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    SmallTopAppBar(
        title = { Text("Dictionaries") },
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

@Composable
fun RemoveDictDialog(model: DictsViewModel) {
    val dialogShown: Boolean by model.removeDialogShown.observeAsState(false)
    if (!dialogShown) return

    AlertDialog(
        title = { Text("Are you sure you want to remove the dict?") },
        icon = { Icon(Icons.Default.Warning, "") },
        confirmButton = {
            Button({ model.removeDict() }) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button({ model.removeDialogShown.value = false }) {
                Text("No")
            }
        },
        onDismissRequest = { model.removeDialogShown.value = false },
    )
}

@Composable
@Preview
fun RemoveDictDialogPreview() {
    val model = DictsViewModel()
    model.removeDialogShown.value = true

    StarDictTheme {
        RemoveDictDialog(model)
    }
}

@Composable
fun DictsList(model: DictsViewModel, padding: PaddingValues) {
    val dicts: List<StarDict> by model.dicts.observeAsState(listOf())

    if (model.dicts.value!!.isEmpty()) {
        Text(
            """
            No dictionaries.

            To add a dictionary press + and choose all the dictionary files, such as:
            .ifo, .idx and .dict

            Note: you have to choose all the files in one go.
            """.trimIndent(),
            modifier = Modifier
                .padding(horizontal = 60.dp)
                .fillMaxHeight()
                .wrapContentHeight()
        )
    }

    Column(modifier = Modifier.padding(padding)) {
        for (dict in dicts)
            DictItem(dict.name) {
                model.removeDialogShown.value = true
            }
    }
}

@Composable
fun DictItem(name: String, onRemoveDict: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_book),
                tint = Color.White,
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            )

            Text(name)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onRemoveDict) {
                Icon(
                    Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = "remove dict",
                )
            }
        }
    }
}
