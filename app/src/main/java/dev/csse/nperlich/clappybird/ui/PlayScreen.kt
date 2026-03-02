package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.csse.nperlich.clappybird.GameViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Preview(
    showBackground = true,
    heightDp = 800,
    widthDp = 400
)
@Composable
fun PlayScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel()
) {
    // start game loop
    LaunchedEffect(Unit) {
        viewModel.startGameLoop()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                viewModel.flap()
            }
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

        // current score
        Text(
            text = "Score: ${viewModel.currentScore}",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        // high score
        viewModel.highScore.value?.let { highScore ->
            Text(
                text = "High: ${highScore.score} by ${highScore.username}",
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }
    }
}


