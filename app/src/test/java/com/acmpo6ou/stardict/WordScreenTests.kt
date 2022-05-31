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

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.acmpo6ou.stardict.screens.FavoritesViewModel
import com.acmpo6ou.stardict.screens.SettingsViewModel
import com.acmpo6ou.stardict.screens.WordParams
import com.acmpo6ou.stardict.screens.WordScreen
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WordScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private lateinit var model: FavoritesViewModel

    @Before
    fun setup() {
        model = FavoritesViewModel()
    }

    @Test
    fun `favorites button`() {
        val params = WordParams(
            "apple", "[trans]",
            mapOf("Universal" to "Article...")
        )
        composeTestRule.setContent {
            val activity = LocalContext.current as MainActivity
            StarDictTheme {
                WordScreen(params, activity, SettingsViewModel(), model)
            }
        }

        // "apple" is not in favorites, so there should be
        // an 'add to favorites' button
        composeTestRule
            .onNodeWithContentDescription("add to favorites")
            .assertExists()

        // add "apple" to favorites
        composeTestRule
            .onNodeWithContentDescription("add to favorites")
            .performClick()

        // the button should change to 'remove from favorites'
        composeTestRule
            .onNodeWithContentDescription("remove from favorites")
            .assertExists()

        // remove "apple" from favorites
        composeTestRule
            .onNodeWithContentDescription("remove from favorites")
            .performClick()

        // the button should change back to 'add to favorites'
        composeTestRule
            .onNodeWithContentDescription("add to favorites")
            .assertExists()
    }
}
