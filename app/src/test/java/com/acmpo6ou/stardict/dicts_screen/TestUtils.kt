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

import com.github.javafaker.Faker
import java.io.File

fun Faker.str(): String = this.lorem().sentence()

const val srcDir = "/tmp/StarDict"

fun setupSrcDir() {
    val dir = File(srcDir)
    dir.deleteRecursively()
    dir.mkdir()
}

/**
 * Copies all dict files to SRC_DIR.
 */
fun copyDict(name: String) {
    for (ext in listOf("ifo", "idx", "dict")) {
        val target = File("sampledata/$name.$ext")
        val destination = File("$srcDir/$name.$ext")
        target.copyTo(destination)
    }
}
