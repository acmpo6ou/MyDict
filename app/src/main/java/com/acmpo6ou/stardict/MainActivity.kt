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
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.navigation.navman

class MainActivity : ComponentActivity() {
    private val importLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK)
                result.data?.data?.let {}
        }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navman.activity = this

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(topBar = { AppBar() }) {
        Column(modifier = Modifier.padding(it)) {
            SearchField()
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
            ) {
                // TODO: use items() and a view model
                items(10) { i ->
                    Row(
                        modifier = Modifier
                            .clickable { /*TODO: go to WordScreen*/ }
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("apple $i", fontSize = 30.sp)
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
        MainScreen()
    }
}

@Composable
fun AppBar() {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    SmallTopAppBar(
        title = { Text("✨StarDict✨") },
        actions = {
            IconButton(
                enabled = true,
                onClick = { navman.goto(composableResId = NavIDs.DictsScreen) }
            ) {
                Icon(
                    painterResource(R.drawable.ic_dict),
                    tint = Color.White,
                    contentDescription = "",
                )
            }

            IconButton(
                enabled = true,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    Icons.Default.Settings,
                    tint = Color.White,
                    contentDescription = "",
                )
            }

            IconButton(
                enabled = true,
                onClick = { navman.goto(composableResId = NavIDs.AboutScreen) }
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
fun SearchField() {
    var searchText by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = searchText,
        onValueChange = {
            searchText = it
            // TODO: search word in dicts
        },
        label = { Text("Search") },
        leadingIcon = {
            // TODO: if searchText is empty use Search icon,
            //  otherwise – X icon, which will clear the text once clicked
            Icon(Icons.Default.Search, "")
        }
    )
}
