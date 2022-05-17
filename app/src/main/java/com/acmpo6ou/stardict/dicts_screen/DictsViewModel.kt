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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acmpo6ou.stardict.MyApp
import io.github.eb4j.stardict.StarDictDictionary
import java.io.File
import java.io.FileInputStream

open class DictsViewModel : ViewModel() {
    lateinit var app: MyApp
    val dicts = MutableLiveData<MutableSet<StarDictDictionary>>(mutableSetOf())
    val error = MutableLiveData("")

    /**
     * Copies dict files from [data] to SRC_DIR.
     * @param data contains URIs with paths to dict files.
     */
    open fun copyDictFiles(data: ClipData) {
        for (i in 0 until data.itemCount) {
            val uri = data.getItemAt(i).uri
            val ext = File(uri.path!!).extension

            // skip all non dict files
            if (ext !in listOf("idx", "ifo", "dict"))
                continue

            val name = File(uri.path!!).name
            val file = File("${app.SRC_DIR}/$name")
            val descriptor = app.contentResolver.openFileDescriptor(uri, "r")

            FileInputStream(descriptor?.fileDescriptor).use {
                file.writeBytes(it.readBytes())
            }
        }
    }

    /**
     * Copies dict files using [copyDictFiles] handling all errors.
     */
    fun importDict(data: ClipData) {
        try {
            copyDictFiles(data)
        } catch (e: Exception) {
            e.printStackTrace()
            error.value = e.toString()
            return
        }

        val path = data.getItemAt(0).uri.path!!
        val name = File(path).name
        val file = File("${app.SRC_DIR}/$name")
        val dict = StarDictDictionary.loadDictionary(file)
        dicts.addItem(dict)
    }
}
