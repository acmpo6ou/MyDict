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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.acmpo6ou.stardict.dicts_screen.DictsViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `search should suggest completions`() {
        setupSrcDir()
        copyDict("ER-Computer")
        copyDict("ER-LingvoScience")

        val dictsViewModel = DictsViewModel()
        dictsViewModel.app = mock { on { SRC_DIR } doReturn srcDir }
        dictsViewModel.loadDicts()

        val model = MainViewModel()
        model.dictsViewModel = dictsViewModel

        /* ER-Computer has this words matching "apple":
        * applet
        * applet viewer
        */

        /* ER-LingvoScience has this words matching "apple":
        * apple-blossom weevil
        */

        // completions should combine words from both dictionaries
        model.search("apple")

        assertEquals(
            listOf(
                "apple-blossom weevil",
                "applet",
                "applet viewer",
            ),
            model.completions.value!!
        )
    }
}
