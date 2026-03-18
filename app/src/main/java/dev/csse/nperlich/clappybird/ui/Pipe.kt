package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Pipe(
    x: Dp,
    y: Dp,
    height: Dp,
    isUpsideDown: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.offset(x = x, y = y),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isUpsideDown) {
            // Lip at bottom (for top pipe)
            Box(
                modifier = Modifier
                    .width(95.dp)
                    .height(30.dp)
                    .background(Color(0xFF00AA00))  // Dark green
            )
            // Main pipe body
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(height)
                    .background(Color(0xFF00DD00))  // Bright green
            )
        } else {
            // Main pipe body
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(height)
                    .background(Color(0xFF00DD00))  // Bright green
            )
            // Lip at top (for bottom pipe)
            Box(
                modifier = Modifier
                    .width(95.dp)
                    .height(30.dp)
                    .background(Color(0xFF00AA00))  // Dark green
            )
        }
    }
}