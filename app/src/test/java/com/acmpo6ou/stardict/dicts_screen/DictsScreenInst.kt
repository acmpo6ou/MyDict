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

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.acmpo6ou.stardict.MainActivity
import com.acmpo6ou.stardict.copyDict
import com.acmpo6ou.stardict.setupSrcDir
import com.acmpo6ou.stardict.srcDir
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class DictsScreenInst {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    lateinit var model: DictsViewModel

    fun setupDictModel() {
        copyDict("ER-Americana")
        copyDict("ER-Computer")
        copyDict("ER-LingvoScience")

        model = DictsViewModel()
        model.app = mock { on { SRC_DIR } doReturn srcDir }
        model.loadDicts()
    }

    @Before
    fun setup() {
        setupSrcDir()
        setupDictModel()
    }

    @Test
    fun `DictsScreen FAB should launch an import dict intent`() {
        composeTestRule.setContent {
            val activity = LocalContext.current as MainActivity
            StarDictTheme { DictsScreen(activity, activity.dictsViewModel) }
        }
        composeTestRule.onNodeWithContentDescription("Import dict")
            .performClick()

        val intent: Intent = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertNotNull(intent)
    }

    @Test
    fun `DictsList should show a message when there are no items`() {
        composeTestRule.setContent {
            StarDictTheme {
                DictsList(DictsViewModel())
            }
        }

        composeTestRule
            .onNodeWithText("No dictionaries", substring = true)
            .assertExists()
    }

    @Test
    fun `DictsList should render items when there are dicts in SRC_DIR`() {
        composeTestRule.setContent {
            StarDictTheme { DictsList(model) }
        }

        // the help message shouldn't be shown
        composeTestRule
            .onNodeWithText("No dictionaries", substring = true)
            .assertDoesNotExist()

        val dictNames = model.dicts.value!!.map { it.dictionaryName }
        for (name in dictNames)
            composeTestRule
                .onNodeWithText(name, useUnmergedTree = true)
                .assertExists()
    }
}
