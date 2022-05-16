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

import android.content.ContentResolver
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.FileInputStream

@RunWith(RobolectricTestRunner::class)
class DictsViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    lateinit var model: DictsViewModel
    private val srcDir = "/tmp/StarDict"

    private val contentResolver: ContentResolver = mock()

    @Before
    fun setupSrcDir() {
        val dir = File(srcDir)
        dir.deleteRecursively()
        dir.mkdir()
    }

    private fun setupInputResolver(location: String, uri: Uri) {
        val descriptor: ParcelFileDescriptor = mock()
        val fis = FileInputStream(File(location))

        whenever(descriptor.fileDescriptor).thenReturn(fis.fd)
        whenever(contentResolver.openFileDescriptor(uri, "r"))
            .thenReturn(descriptor)
    }

    @Before
    fun setup() {
        model = DictsViewModel()
        model.app = mock {
            on { SRC_DIR } doReturn srcDir
            on { contentResolver } doReturn contentResolver
        }
    }

    @Test
    fun `importDict should copy dict files to SRC_DIR`() {}
}
