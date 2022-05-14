package com.acmpo6ou.stardict

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acmpo6ou.stardict.ui.theme.StarDictTheme

class MainActivity : ComponentActivity() {
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
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
            ) {
                // TODO: use items() and a view model
                items(10) { i ->
                    Row(
                        modifier = Modifier
                            .clickable { /*TODO: go to WordScreen*/ }
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("apple $i", fontSize = 30.sp)
                    }
                }
            }
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
fun SearchField() {
    var searchText by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = searchText,
        onValueChange = {
            searchText = it
            // TODO: search word in dicts
        },
        label = { Text("Search") },
        leadingIcon = {
            // TODO: if searchText is empty use Search icon,
            //  otherwise – X icon, which will clear the text once clicked
            Icon(Icons.Default.Search, "")
        }
    )
}
