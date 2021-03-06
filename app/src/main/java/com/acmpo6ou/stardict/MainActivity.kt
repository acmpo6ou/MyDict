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
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.acmpo6ou.stardict.dicts_screen.DictsViewModel
import com.acmpo6ou.stardict.screens.FavoritesViewModel
import com.acmpo6ou.stardict.screens.SettingsViewModel
import com.acmpo6ou.stardict.screens.WordParams
import com.acmpo6ou.stardict.ui.theme.DarkGrey
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.navigation.navman
import java.io.File
import java.util.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels()
    val dictsViewModel: DictsViewModel by viewModels()
    val settingsViewModel: SettingsViewModel by viewModels()
    val favoritesViewModel: FavoritesViewModel by viewModels()

    lateinit var tts: TextToSpeech
    private val importLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK)
                result.data?.clipData?.let { dictsViewModel.importDict(it) }
        }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalTextApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        navman.activity = this

        dictsViewModel.app = applicationContext as MyApp
        dictsViewModel.loadDicts()
        mainViewModel.dictsViewModel = dictsViewModel

        settingsViewModel.prefs = getPreferences(Context.MODE_PRIVATE)
        settingsViewModel.loadPrefs()
        settingsViewModel.fonts = File("/system/fonts")
            .listFiles()?.associateWith { Font(it) } ?: mapOf()

        val path = getExternalFilesDir(null)!!.path + "/favorites.txt"
        favoritesViewModel.loadFavorites(path)

        if (navman.totalScreensDisplayed == 0)
            navman.goto(composableResId = NavIDs.MainScreen)

        setContent {
            StarDictTheme(settingsViewModel) {
                ScreenFactoryHandler()
            }
        }

        handleSelectedText()
        loadTTS()
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

    private fun loadTTS() {
        tts = TextToSpeech(this) {
            tts.language = Locale.US
            tts.speak("", TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus ?: return
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    /**
     * Handles cases when user started the app by selecting some text,
     * and choosing `MyDict` in the context menu.
     */
    private fun handleSelectedText() {
        var text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            ?: return
        text = text.toString().lowercase().trim()
        mainViewModel.search(text)

        if (mainViewModel.completions.value!!.isEmpty())
            return

        navman.goto(
            composableResId = NavIDs.WordScreen,
            p = WordParams(
                text, mainViewModel.getTranscription(text),
                mainViewModel.getArticles(text),
            )
        )

        lifecycleScope.launch {
            // wait while the app loads, otherwise the keyboard won't be hidden
            delay(500)
            hideKeyboard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(activity: MainActivity, model: MainViewModel) {
    val completions: List<String> by model.completions.observeAsState(listOf())

    Scaffold(topBar = { MainAppBar(activity) }) {
        Column(modifier = Modifier.padding(it)) {
            SearchField(model, activity)

            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(completions.size) { i ->
                    val word = completions[i]
                    Row(
                        modifier = Modifier
                            .clickable {
                                activity.hideKeyboard()
                                navman.goto(
                                    composableResId = NavIDs.WordScreen,
                                    p = WordParams(
                                        word, model.getTranscription(word),
                                        model.getArticles(word),
                                    )
                                )
                            }
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(word, fontSize = 26.sp)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    val model = MainViewModel()
    model.completions.value = listOf("apple", "apple pie", "apple jack")
    StarDictTheme {
        MainScreen(MainActivity(), model)
    }
}

@Composable
fun MainAppBar(activity: MainActivity) {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )
    var showMenu by remember { mutableStateOf(false) }

    SmallTopAppBar(
        title = { Text("MyDict") },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    Icons.Default.MoreVert,
                    "",
                    tint = Color.White,
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(DarkGrey)
            ) {
                DropdownMenuItem(
                    text = { Text("Dictionaries", fontSize = 20.sp) },
                    onClick = {
                        showMenu = false
                        activity.hideKeyboard()
                        navman.goto(composableResId = NavIDs.DictsScreen)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Favorites", fontSize = 20.sp) },
                    onClick = {
                        showMenu = false
                        activity.hideKeyboard()
                        navman.goto(composableResId = NavIDs.FavoritesScreen)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Settings", fontSize = 20.sp) },
                    onClick = {
                        showMenu = false
                        activity.hideKeyboard()
                        navman.goto(composableResId = NavIDs.SettingsScreen)
                    }
                )
                DropdownMenuItem(
                    text = { Text("About", fontSize = 20.sp) },
                    onClick = {
                        showMenu = false
                        activity.hideKeyboard()
                        navman.goto(composableResId = NavIDs.AboutScreen)
                    }
                )
            }
        },
        colors = colors,
    )
}

@Composable
fun SearchField(model: MainViewModel, activity: MainActivity) {
    val searchText: String by model.searchText.observeAsState("")
    val focusRequester = remember { FocusRequester() }

    val scope = rememberCoroutineScope()
    var currentJob by remember { mutableStateOf<Job?>(null) }
    val SEARCH_DELAY_MILLIS = 300L

    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = searchText,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        maxLines = 1,
        onValueChange = {
            model.searchText.value = it
            currentJob?.cancel()
            currentJob = scope.async {
                delay(SEARCH_DELAY_MILLIS)
                model.search(it)
            }
        },
        label = { Text("Search") },
        leadingIcon = {
            Icon(Icons.Default.Search, "")
        },
        trailingIcon = {
            if (searchText.isNotEmpty())
                IconButton(onClick = {
                    model.searchText.value = ""
                    model.completions.value = listOf()

                    focusRequester.requestFocus()
                    val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(
                        activity.currentFocus,
                        InputMethodManager.SHOW_IMPLICIT,
                    )
                }) {
                    Icon(Icons.Default.Clear, "clear search")
                }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
