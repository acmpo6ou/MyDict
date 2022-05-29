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

package com.acmpo6ou.stardict.utils

import android.app.Activity
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.acmpo6ou.stardict.MainActivity
import com.vanpra.composematerialdialogs.MaterialDialogScope
import dev.wirespec.jetmagic.navigation.navman

@Composable
fun MaterialDialogScope.iconTitle(
    titleText: String,
    icon: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(14.dp))
        Text(
            text = titleText,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
fun MaterialDialogScope.message(text: String? = null, @StringRes res: Int? = null) {
    val messageText = text ?: stringResource(res!!)

    Text(
        text = messageText,
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
    )
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = {
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
            it.textSize = 30f
            it.setTextIsSelectable(true)
            it.setTextColor(android.graphics.Color.parseColor("#ffffff"))
        }
    )
}

/**
 * Makes article more readable by adding new lines where appropriate.
 */
fun formatArticle(text: String): String {
    // remove word and transcription from article
    val regex1 = Regex("<k>(.*?)</k>")
    val regex2 = Regex("<tr>(.*?)</tr>")
    var text = text.replace(regex1, "")
    text = text.replace(regex2, "")

    val regex3 = """(\d\d?\))""".toRegex()
    text = text.replace(regex3, "<br><br>$1")

    val regex4 = """(<ex>)""".toRegex()
    text = text.replace(regex4, "<br><br><ex>")

    val regex5 = """(- <kref>)""".toRegex()
    text = text.replace(regex5, "<br>- <kref>")

    val regex6 = """(\d\d?\.)""".toRegex()
    text = text.replace(regex6, "<br><br>$1")
    return text
}

/**
 * Displays a toast telling the user that volume is off if the volume is off.
 */
fun checkVolume(activity: Activity) {
    val am = activity.getSystemService(AUDIO_SERVICE) as AudioManager
    val volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC)

    if (volume_level == 0)
        Toast
            .makeText(activity, "Volume is off", Toast.LENGTH_SHORT)
            .show()
}

@Composable
fun BackButton() {
    val activity = LocalContext.current as MainActivity

    IconButton(onClick = {
        activity.hideKeyboard()
        navman.goBack()
    }) {
        Icon(
            Icons.Default.ArrowBack,
            tint = Color.White,
            contentDescription = "go back",
        )
    }
}
