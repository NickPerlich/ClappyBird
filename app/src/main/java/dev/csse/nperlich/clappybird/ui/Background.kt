package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import dev.csse.nperlich.clappybird.R

@Composable
fun Background(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = "Background",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}