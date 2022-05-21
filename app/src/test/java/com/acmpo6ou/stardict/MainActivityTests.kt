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
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `search field should hide or show clear text icon`() {
        composeTestRule.setContent {
            val activity = LocalContext.current as MainActivity
            StarDictTheme { MainScreen(activity.mainViewModel) }
        }

        // when search field is empty, there should be no clear text icon
        composeTestRule
            .onNodeWithContentDescription("clear search")
            .assertDoesNotExist()

        // when there is text in the field, there should be a clear text icon
        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("apple")

        composeTestRule
            .onNodeWithContentDescription("clear search")
            .assertExists()
    }
}
