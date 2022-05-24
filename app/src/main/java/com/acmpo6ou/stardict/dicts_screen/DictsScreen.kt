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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acmpo6ou.stardict.*
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.DarkGrey
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.acmpo6ou.stardict.utils.StarDict
import com.acmpo6ou.stardict.utils.iconTitle
import com.acmpo6ou.stardict.utils.message
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
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
        val removeDialogState = rememberMaterialDialogState()
        val errorDialogState = rememberMaterialDialogState()
        model.errorDialog = errorDialogState

        RemoveDictDialog(removeDialogState, model)
        ErrorDialog(errorDialogState, model)
        DictsList(removeDialogState, model, it)
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
fun RemoveDictDialog(
    removeDialogState: MaterialDialogState,
    model: DictsViewModel,
) {
    MaterialDialog(
        removeDialogState,
        backgroundColor = DarkGrey,
        buttons = {
            positiveButton(
                "Yes",
                onClick = { model.deleteDict() },
                textStyle = TextStyle(MaterialTheme.colorScheme.primary),
            )
            negativeButton(
                "No",
                textStyle = TextStyle(MaterialTheme.colorScheme.primary),
            )
        }
    ) {
        iconTitle("Warning!") {
            Icon(Icons.Default.Warning, "")
        }
        message(stringResource(R.string.remove_dict_message))
    }
}

@Composable
@Preview
fun RemoveDictDialogPreview() {
    val state = MaterialDialogState()
    StarDictTheme {
        RemoveDictDialog(state, DictsViewModel())
    }
    state.show()
}

@Composable
fun ErrorDialog(
    dialogState: MaterialDialogState,
    model: DictsViewModel,
) {
    val errorMessage: String by model.error.observeAsState("")

    MaterialDialog(
        dialogState,
        backgroundColor = DarkGrey,
        buttons = {
            positiveButton(
                "OK",
                textStyle = TextStyle(MaterialTheme.colorScheme.primary),
            )
        }
    ) {
        iconTitle("Error!") {
            Icon(painterResource(R.drawable.ic_error), "")
        }
        message(errorMessage)
    }
}

@Composable
@Preview
fun ErrorDialogPreview() {
    val state = MaterialDialogState()
    val model = DictsViewModel()
    model.error.value = "An error occurred!"
    StarDictTheme {
        ErrorDialog(state, model)
    }
    state.show()
}

@Composable
fun DictsList(
    removeDialogState: MaterialDialogState,
    model: DictsViewModel,
    padding: PaddingValues,
) {
    val dicts: List<StarDict> by model.dicts.observeAsState(listOf())

    if (model.dicts.value!!.isEmpty()) {
        Text(
            stringResource(R.string.no_dicts),
            modifier = Modifier
                .padding(horizontal = 60.dp)
                .fillMaxHeight()
                .wrapContentHeight()
        )
    }

    Column(modifier = Modifier.padding(padding)) {
        for (dict in dicts)
            DictItem(dict.name) {
                model.dictToRemove.value = dict
                removeDialogState.show()
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
