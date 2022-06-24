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

import com.acmpo6ou.stardict.utils.StarDict
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class StarDictTests {
    @Before
    fun setup() {
        setupSrcDir()
    }

    @Test
    fun `loadIfoFile should load name of the dictionary`() {
        copyDict("ER-LingvoUniversal")
        val dict = StarDict()
        dict.initialize("$srcDir/ER-LingvoUniversal")
        assertEquals("LingvoUniversal (En-Ru)", dict.name)
    }

    @Test
    fun `loadContentFile should load correct file, dict dz or dict`() {
        // .dict.dz should be loaded
        copyDict("ER-LingvoUniversal")
        StarDict().loadContentFile("$srcDir/ER-LingvoUniversal")

        // .dict should be loaded
        copyDict("ER-Computer")
        StarDict().loadContentFile("$srcDir/ER-Computer")
    }

    @Test
    fun `getArticle should return null if the word is not found`() {
        copyDict("ER-Computer")
        val dict = StarDict()
        dict.initialize("$srcDir/ER-Computer")

        dict.getSuggestions("zoological")
        val article = dict.getArticle("zoological")
        assertNull(article)
    }
}
