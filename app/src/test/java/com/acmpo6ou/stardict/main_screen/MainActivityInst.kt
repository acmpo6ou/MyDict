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

package com.acmpo6ou.stardict.main_screen

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.acmpo6ou.stardict.MainActivity
import com.acmpo6ou.stardict.MainScreen
import com.acmpo6ou.stardict.MainViewModel
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityInst {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `completions list should render its items`() {
        val model = MainViewModel()
        composeTestRule.setContent {
            val activity = LocalContext.current as MainActivity
            StarDictTheme { MainScreen(activity, model) }
        }

        model.completions.value = listOf("apple", "apple pie", "apple jack")
        for (completion in model.completions.value!!)
            composeTestRule
                .onNodeWithText(completion)
                .assertExists()
    }
}
