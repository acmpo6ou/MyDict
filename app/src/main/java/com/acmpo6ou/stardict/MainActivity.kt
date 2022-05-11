package com.acmpo6ou.stardict

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.acmpo6ou.stardict.ui.theme.StarDictTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarDictTheme {
                AppBar()
            }
        }
    }
}

@Composable
fun AppBar() {
    SmallTopAppBar(
        title = { Text("✨StarDict✨") }
    )
}

@Preview
@Composable
fun AppBarPreview() {
    AppBar()
}
