package com.acmpo6ou.stardict.dicts_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.acmpo6ou.stardict.AppBar
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme

@Composable
fun DictsScreen() {
    AppBar()
    DictsList(DictsViewModel())
    // TODO: add FAB
}

@Composable
fun DictsList(model: DictsViewModel) {
    // TODO: use for loop
}

@Composable
fun DictItem(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_book),
                tint = Color.White,
                contentDescription = "",
            )

            Text(name)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Default.Delete,
                    tint = Color.White,
                    contentDescription = "",
                )
            }
        }
    }
}

@Preview
@Composable
fun DictItemPreview() {
    StarDictTheme() {
        DictItem("Universal")
    }
}
