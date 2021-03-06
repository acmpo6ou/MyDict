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

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import com.acmpo6ou.stardict.dicts_screen.DictsScreen
import com.acmpo6ou.stardict.screens.*
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.initializeJetmagic
import dev.wirespec.jetmagic.models.ComposableResource

open class MyApp : Application() {
    open val SRC_DIR get() = getExternalFilesDir(null)?.path + "/"

    override fun onCreate() {
        super.onCreate()
        initializeJetmagic(this)

        crm.apply {
            addComposableResources(
                mutableListOf(
                    ComposableResource(NavIDs.MainScreen) {
                        val activity = LocalContext.current as MainActivity
                        MainScreen(activity, activity.mainViewModel)
                    },
                    ComposableResource(NavIDs.DictsScreen) {
                        val activity = LocalContext.current as MainActivity
                        DictsScreen(activity, activity.dictsViewModel)
                    },
                    ComposableResource(NavIDs.WordScreen) {
                        val activity = LocalContext.current as MainActivity
                        WordScreen(
                            it.parameters as WordParams,
                            activity, activity.settingsViewModel,
                            activity.favoritesViewModel,
                        )
                    },
                    ComposableResource(NavIDs.FavoritesScreen) {
                        val activity = LocalContext.current as MainActivity
                        FavoritesScreen(activity, activity.favoritesViewModel)
                    },
                    ComposableResource(NavIDs.SettingsScreen) {
                        val activity = LocalContext.current as MainActivity
                        SettingsScreen(activity.settingsViewModel, activity)
                    },
                    ComposableResource(NavIDs.AboutScreen) {
                        val activity = LocalContext.current as MainActivity
                        AboutScreen(activity)
                    },
                )
            )
        }
    }
}
