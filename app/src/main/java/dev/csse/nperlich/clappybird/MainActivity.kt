package dev.csse.nperlich.clappybird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.csse.nperlich.clappybird.ui.ClappyBirdApp
import dev.csse.nperlich.clappybird.ui.theme.ClappyBirdTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClappyBirdApp()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ClappyBirdPreview() {
    ClappyBirdApp()
}