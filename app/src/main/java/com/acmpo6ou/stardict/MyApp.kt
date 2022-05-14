package com.acmpo6ou.stardict

import android.app.Application
import com.acmpo6ou.stardict.dicts_screen.DictsScreen
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.initializeJetmagic
import dev.wirespec.jetmagic.models.ComposableResource

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeJetmagic(this)

        crm.apply {
            addComposableResources(
                mutableListOf(
                    ComposableResource(NavIDs.MainScreen) {
                        MainScreen()
                    },
                    ComposableResource(NavIDs.DictsScreen) {
                        DictsScreen()
                    },
                )
            )
        }
    }
}
