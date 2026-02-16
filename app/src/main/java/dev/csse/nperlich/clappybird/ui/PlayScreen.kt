package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(
    showBackground = true,
    heightDp = 800,
    widthDp = 400
)
@Composable
fun PlayScreen(
    modifier: Modifier = Modifier,
    onDieClick: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Bird - yellow circle
        Box(
            modifier = Modifier
                .offset(x = 100.dp, y = 300.dp)
                .size(40.dp)
                .background(Color.Yellow, shape = CircleShape)
        )

        // Pipe 1 - top rectangle
        Box(
            modifier = Modifier
                .offset(x = 250.dp, y = 0.dp)
                .width(80.dp)
                .height(400.dp)
                .background(Color.Green)
        )

        // Pipe 1 - bottom rectangle (gap in between)
        Box(
            modifier = Modifier
                .offset(x = 250.dp, y = 600.dp)
                .width(80.dp)
                .height(400.dp)
                .background(Color.Green)
        )

        // Die - button on bottom right
        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onDieClick,
        ) {
            Text("Die")
        }
    }
}


