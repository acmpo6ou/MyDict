package com.acmpo6ou.stardict

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.acmpo6ou.stardict.ui.theme.StarDictTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarDictTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = { AppBar() },
    ) {
        Column(modifier = Modifier.padding(it)) {
            SearchField()
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}

@Composable
fun AppBar() {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    SmallTopAppBar(
        title = { Text("✨StarDict✨") },
        actions = {
            IconButton(
                enabled = true,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painterResource(R.drawable.ic_dict),
                    tint = Color.White,
                    contentDescription = "",
                )
            }

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
        },
        colors = colors,
    )
}

@Preview
@Composable
fun AppBarPreview() {
    AppBar()
}

@Composable
fun SearchField() {
    var searchText by rememberSaveable { mutableStateOf("") }
    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            // TODO: search word in dicts
        },
        label = { Text("Search") }
    )
}

@Composable
@Preview
fun SearchFieldPreview() {
    SearchField()
}
