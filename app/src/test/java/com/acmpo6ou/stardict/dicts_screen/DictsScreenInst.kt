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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.acmpo6ou.stardict.MainActivity
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.copyDict
import com.acmpo6ou.stardict.setupSrcDir
import com.acmpo6ou.stardict.srcDir
import com.acmpo6ou.stardict.ui.theme.StarDictTheme
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.vanpra.composematerialdialogs.MaterialDialogState
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import java.io.File

@RunWith(RobolectricTestRunner::class)
class DictsScreenInst {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    lateinit var model: DictsViewModel

    private fun setupDictModel() {
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
        var noDictsMsg = ""
        composeTestRule.setContent {
            noDictsMsg = stringResource(R.string.no_dicts)
            val state = MaterialDialogState()
            StarDictTheme {
                DictsList(state, DictsViewModel(), PaddingValues(0.dp))
            }
        }

        composeTestRule
            .onNodeWithText(noDictsMsg)
            .assertExists()
    }

    @Test
    fun `DictsList should render items when there are dicts in SRC_DIR`() {
        var noDictsMsg = ""
        composeTestRule.setContent {
            noDictsMsg = stringResource(R.string.no_dicts)
            val state = MaterialDialogState()
            StarDictTheme { DictsList(state, model, PaddingValues(0.dp)) }
        }

        // the help message shouldn't be shown
        composeTestRule
            .onNodeWithText(noDictsMsg, substring = true)
            .assertDoesNotExist()

        val dictNames = model.dicts.value!!.map { it.name }
        for (name in dictNames)
            composeTestRule
                .onNodeWithText(name, useUnmergedTree = true)
                .assertExists()
    }

    @Test
    fun `pressing remove dict button should show RemoveDictDialog`() {
        var removeDictMessage = ""
        composeTestRule.setContent {
            val activity = LocalContext.current as MainActivity
            activity.dictsViewModel.app = model.app
            activity.dictsViewModel.loadDicts()

            removeDictMessage = stringResource(R.string.remove_dict_message)
            DictsScreen(activity, activity.dictsViewModel)
        }

        composeTestRule
            .onAllNodesWithContentDescription("remove dict")
            .onFirst()
            .performClick()

        composeTestRule
            .onNodeWithText(removeDictMessage)
            .assertExists()

        // dict files should be removed when Yes is chosen in the dialog
        composeTestRule
            .onNodeWithText("YES")
            .performClick()

        for (ext in listOf("ifo", "idx", "dict")) {
            val file = File("$srcDir/ER-Americana.$ext")
            assert(!file.exists())
        }

        // the dict should be removed from the list
        composeTestRule
            .onNodeWithText("Americana (En-Ru)")
            .assertDoesNotExist()

        // and the dialog should be hidden
        composeTestRule
            .onNodeWithText(removeDictMessage)
            .assertDoesNotExist()
    }
}
