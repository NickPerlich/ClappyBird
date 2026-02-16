package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.csse.nperlich.clappybird.GameViewModel

@Preview(
    showBackground = true,
    heightDp = 800,
    widthDp = 400
)
@Composable
fun PlayScreen(
    modifier: Modifier = Modifier,
    onDieClick: () -> Unit = {},
    viewModel: GameViewModel = viewModel()
) {
    // start game loop
    LaunchedEffect(Unit) {
        viewModel.startGameLoop()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { viewModel.flap() }
    ) {
        // Bird - yellow circle
        Box(
            modifier = Modifier
                .offset(x = 100.dp, y = viewModel.birdY.dp)
                .size(40.dp)
                .background(Color.Yellow, shape = CircleShape)
        )

        // Pipe - top rectangle
        Box(
            modifier = Modifier
                .offset(x = viewModel.pipeX.dp, y = 0.dp)
                .width(80.dp)
                .height(viewModel.gapY.dp)
                .background(Color.Green)
        )

        // Pipe - bottom rectangle (gap in between)
        Box(
            modifier = Modifier
                .offset(
                    x = viewModel.pipeX.dp,
                    y = (viewModel.gapY + viewModel.gapSize).dp
                )
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


