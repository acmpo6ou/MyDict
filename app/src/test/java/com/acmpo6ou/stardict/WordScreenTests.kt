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

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.acmpo6ou.stardict.word_screen.WordParams
import com.acmpo6ou.stardict.word_screen.WordScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WordScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `WordScreen should load word data`() {
        composeTestRule.setContent {
            val params = WordParams(
                "apple", "[trans]",
                mapOf(
                    "Universal" to "<k>apple</k><tr>trans</tr>Article...",
                    "Computer" to "Computer article..."
                )
            )
            StarDictTheme { WordScreen(params) }
        }

        composeTestRule.onNodeWithText("apple").assertExists()
        composeTestRule.onNodeWithText("[trans]").assertExists()

        composeTestRule.onNodeWithText("Universal").assertExists()
        composeTestRule.onNodeWithText("Article...").assertExists()
        composeTestRule.onNodeWithText("Computer").assertExists()
        composeTestRule.onNodeWithText("Computer article...").assertExists()

        // the transcription and word should be removed
        composeTestRule.onNodeWithText("<k>apple</k>", substring = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("<tr>trans</tr>", substring = true)
            .assertDoesNotExist()
    }
}
