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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    lateinit var model: MainViewModel

    @Before
    fun setup() {
        model = MainViewModel()
        val dictsViewModel = DictsViewModel()

        dictsViewModel.app = mock { on { SRC_DIR } doReturn srcDir }
        model.dictsViewModel = dictsViewModel

        setupSrcDir()
        copyDict("ER-Computer")
        copyDict("ER-LingvoUniversal")
        dictsViewModel.loadDicts()
    }

    @Test
    fun `getArticles should return a map of dict names to word articles`() {
        val computerArticle =
            """<k>Apple</k>
 <i><dtrn><co>корпорация Apple Computer - производитель вычислительной техники и программного обеспечения, а также принадлежащая ей торговая марка</co></dtrn></i>
 <dtrn>(web-site: <iref>http://www.apple.com</iref>)</dtrn>"""

        val lingvoArticle =
            """<k>apple</k>
<tr>ˈæpl</tr> <rref>apple.wav</rref>
 <abr><i><c><co>сущ.</co></c></i></abr>
 1) <dtrn>яблоко</dtrn>
  <ex>Most of our best apples are supposed to have been introduced into Britain by a fruiterer of Henry the Eighth. — Считается, что все наши лучшие сорта яблок были ввезены в Великобританию торговцем фруктами при Генрихе Восьмом.</ex>
 2) <dtrn>яблоня</dtrn>
 ••
 <ex>the rotten apple injures its neighbours <abr><i><c><co>посл.</co></c></i></abr> — паршивая овца все стадо портит</ex>
 - <kref>alley apple</kref>
 - <kref>apple of discord</kref>
- <kref>apple of one&apos;s eye</kref>
- <kref>Big Apple</kref>
 - <kref>road apple</kref>"""

        assertEquals(
            mapOf(
                "LingvoComputer (En-Ru)" to computerArticle,
                "LingvoUniversal (En-Ru)" to lingvoArticle,
            ),
            model.getArticles("apple")
        )
    }

    @Test
    fun `getTranscription should return word transcription`() {
        assertEquals("ˈæpl", model.getTranscription("apple"))
    }
}
