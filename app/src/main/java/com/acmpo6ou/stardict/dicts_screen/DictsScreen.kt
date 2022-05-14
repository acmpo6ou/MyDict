package com.acmpo6ou.stardict.dicts_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.acmpo6ou.stardict.R
import com.acmpo6ou.stardict.ui.theme.StarDictTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictsScreen() {
    Scaffold(
        topBar = { DictsAppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
            ) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        DictsList(DictsViewModel())
    }
}

@Composable
@Preview
fun DictsScreenPreview() {
    StarDictTheme {
        DictsScreen()
    }
}

@Composable
fun DictsAppBar() {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    SmallTopAppBar(
        title = { Text("Dictionaries") },
        actions = {
            IconButton(
                enabled = true,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    Icons.Default.Settings,
                    tint = Color.White,
                    contentDescription = "",
                )
            }

            IconButton(
                enabled = true,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    Icons.Default.Info,
                    tint = Color.White,
                    contentDescription = "",
                )
            }
        },
        colors = colors,
    )
}

@Composable
fun DictsList(model: DictsViewModel) {
    // TODO: use for loop
}

@Composable
fun DictItem(name: String, onRemoveDict: () -> Unit) {
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

            IconButton(onClick = onRemoveDict) {
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
    StarDictTheme {
        DictItem("Universal") {}
    }
}
