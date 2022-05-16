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

class DictsViewModel : ViewModel() {
    lateinit var app: MyApp
    val dicts = MutableLiveData<List<String>>(listOf())

    /**
     * Copies dict files from [data] to SRC_DIR.
     */
    fun importDict(data: ClipData) {
        // TODO: add the dict to dicts list
    }
}
