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

package com.acmpo6ou.stardict

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.dicts_screen.DictsViewModel
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.navigation.navman

class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels()
    val dictsViewModel: DictsViewModel by viewModels()

    private val importLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK)
                result.data?.clipData?.let { dictsViewModel.importDict(it) }
        }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navman.activity = this

        dictsViewModel.app = applicationContext as MyApp
        dictsViewModel.loadDicts()
        mainViewModel.dictsViewModel = dictsViewModel

        if (navman.totalScreensDisplayed == 0)
            navman.goto(composableResId = NavIDs.MainScreen)

        setContent {
            StarDictTheme {
                ScreenFactoryHandler()
            }
        }
    }

    fun importDictDialog() =
        with(Intent(Intent.ACTION_OPEN_DOCUMENT)) {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            type = "*/*"
            importLauncher.launch(this)
        }

    override fun onBackPressed() {
        if (!navman.goBack())
            super.onBackPressed()
    }

    override fun onDestroy() {
        crm.onConfigurationChanged()
        super.onDestroy()
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(activity: MainActivity, model: MainViewModel) {
    val completions: List<String> by model.completions.observeAsState(listOf())

    Scaffold(topBar = { AppBar(activity) }) {
        Column(modifier = Modifier.padding(it)) {
            SearchField(model)

            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(completions.size) { i ->
                    Row(
                        modifier = Modifier
                            .clickable { /*TODO: go to WordScreen*/ }
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(completions[i], fontSize = 26.sp)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    StarDictTheme {
        MainScreen(MainActivity(), MainViewModel())
    }
}

@Composable
fun AppBar(activity: MainActivity) {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    SmallTopAppBar(
        title = { Text("✨StarDict✨") },
        actions = {
            IconButton(
                enabled = true,
                onClick = {
                    activity.hideKeyboard()
                    navman.goto(composableResId = NavIDs.DictsScreen)
                }
            ) {
                Icon(
                    painterResource(R.drawable.ic_dict),
                    tint = Color.White,
                    contentDescription = "",
                )
            }

            IconButton(
                enabled = true,
                onClick = {
                    activity.hideKeyboard()
                }
            ) {
                Icon(
                    Icons.Default.Settings,
                    tint = Color.White,
                    contentDescription = "",
                )
            }

            IconButton(
                enabled = true,
                onClick = {
                    activity.hideKeyboard()
                    navman.goto(composableResId = NavIDs.AboutScreen)
                }
            ) {
                Icon(
                    Icons.Default.Info,
                    tint = Color.White,
                    contentDescription = "",
                )
            }
        },
        colors = colors,
    )
}

@Composable
fun SearchField(model: MainViewModel) {
    val searchText: String by model.searchText.observeAsState("")
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = searchText,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        maxLines = 1,
        onValueChange = { model.search(it) },
        label = { Text("Search") },
        leadingIcon = {
            Icon(Icons.Default.Search, "")
        },
        trailingIcon = {
            if (searchText.isNotEmpty())
                IconButton(onClick = {
                    model.searchText.value = ""
                    model.completions.value = listOf()
                }) {
                    Icon(Icons.Default.Clear, "clear search")
                }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun BackButton() {
    IconButton(onClick = { navman.goBack() }) {
        Icon(
            Icons.Default.ArrowBack,
            tint = Color.White,
            contentDescription = "go back",
        )
    }
}
