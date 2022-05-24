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

import android.content.ClipData
import android.content.ClipDescription
import android.content.ContentResolver
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.acmpo6ou.stardict.*
import com.acmpo6ou.stardict.utils.StarDict
import com.github.javafaker.Faker
import com.nhaarman.mockitokotlin2.*
import com.vanpra.composematerialdialogs.MaterialDialogState
import org.junit.Assert.assertEquals
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
    private lateinit var model: DictsViewModel

    private val contentResolver: ContentResolver = mock()
    private lateinit var data: ClipData

    private fun setupInputResolver(location: String, uri: Uri) {
        whenever(uri.path).thenReturn(location)
        val descriptor: ParcelFileDescriptor = mock()
        val fis = FileInputStream(File(location))

        whenever(descriptor.fileDescriptor).thenReturn(fis.fd)
        whenever(contentResolver.openFileDescriptor(uri, "r"))
            .thenReturn(descriptor)
    }

    @Before
    fun setup() {
        setupSrcDir()

        model = DictsViewModel()
        model.errorDialog = MaterialDialogState()
        model.app = mock {
            on { SRC_DIR } doReturn srcDir
            on { contentResolver } doReturn contentResolver
        }

        val clipDescription = ClipDescription("", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN))
        val clipList = mutableListOf<ClipData.Item>()

        for (ext in listOf("ifo", "idx", "dict")) {
            val path = "sampledata/ER-LingvoUniversal.$ext"
            val uri = mock<Uri>()
            val item = ClipData.Item(uri)

            setupInputResolver(path, uri)
            clipList.add(item)
        }

        data = ClipData(clipDescription, clipList.first())
        data.addItem(clipList[1])
        data.addItem(clipList[2])
    }

    @Test
    fun `loadDictionary should load a dict from SRC_DIR`() {
        copyDict("ER-Computer")
        model.loadDictionary("ER-Computer")

        assertEquals(
            "LingvoComputer (En-Ru)",
            model.dicts.value!!.first().name
        )
    }

    @Test
    fun `loadDictionary should handle all errors`() {
        // try to load a dict that doesn't have a .ifo file
        copyDict("ER-Computer")
        File("$srcDir/ER-Computer.ifo").delete()
        model.loadDictionary("ER-Computer")

        val msg = "java.io.FileNotFoundException: " +
            "$srcDir/ER-Computer.ifo (No such file or directory)"
        assertEquals(msg, model.error.value)

        // other dict files (.idx and .dict) should be removed from SRC_DIR
        val idxFile = File("$srcDir/ER-Computer.idx")
        val dictFile = File("$srcDir/ER-Computer.dict")
        assert(!idxFile.exists())
        assert(!dictFile.exists())
    }

    @Test
    fun `loadDicts should load dicts from SRC_DIR`() {
        copyDict("ER-Americana")
        copyDict("ER-Computer")
        copyDict("ER-LingvoScience")

        model.loadDicts()
        assertEquals(
            setOf(
                "Americana (En-Ru)",
                "LingvoComputer (En-Ru)",
                "LingvoScience (En-Ru)"
            ),
            model.dicts.value!!.map { it.name }.toSet(),
        )
    }

    @Test
    fun `importDict should copy dict files to SRC_DIR and update dicts list`() {
        model.importDict(data)

        // the files should be copied
        for (ext in listOf("ifo", "idx", "dict")) {
            val file = File("$srcDir/ER-LingvoUniversal.$ext")
            assert(file.exists())
        }

        // the dicts list should contain the imported dict
        val dict = StarDict()
        dict.initialize("$srcDir/ER-LingvoUniversal")
        assertEquals(dict.name, model.dicts.value!!.first().name)
    }

    @Test
    fun `importDict should copy only ifo, idx and dict files`() {
        // besides .ifo, .idx and .dict files, the user chose another file
        val uri = mock<Uri>()
        val item = ClipData.Item(uri)

        setupInputResolver("build.gradle", uri)
        data.addItem(item)

        // the .ifo, .idx and .dict files should be copied to SRC_DIR
        model.importDict(data)
        for (ext in listOf("ifo", "idx", "dict")) {
            val file = File("$srcDir/ER-LingvoUniversal.$ext")
            assert(file.exists())
        }

        // but the build.gradle file should be skipped
        val file = File("$srcDir/build.gradle")
        assert(!file.exists())
    }

    @Test
    fun `importDict should handle all errors`() {
        val msg = Faker().str()
        val exception = Exception(msg)

        val spyModel = spy(model)
        doAnswer { throw exception }.whenever(spyModel).copyDictFiles(data)

        spyModel.importDict(data)
        assertEquals(exception.toString(), spyModel.error.value!!)
    }
}
