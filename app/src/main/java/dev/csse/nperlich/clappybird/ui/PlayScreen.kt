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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.csse.nperlich.clappybird.GameViewModel
import dev.csse.nperlich.clappybird.SoundDetector
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
    var permissionGranted by remember { mutableStateOf(false) }

    // Request permission when screen appears
    if (!permissionGranted) {
        RequestAudioPermission(
            onPermissionGranted = { permissionGranted = true }
        )
    }

    // Start game loop and sound detection when permission granted
    DisposableEffect(permissionGranted) {
        if (permissionGranted) {
            viewModel.startGameLoop()

            val soundDetector = SoundDetector(
                onClapDetected = { viewModel.flap() }
            )
            soundDetector.startListening()

            // Cleanup when leaving screen
            onDispose {
                soundDetector.stopListening()
            }
        } else {
            onDispose { }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
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

        // Pipe 2 - top rectangle
        Box(
            modifier = Modifier
                .offset(x = viewModel.pipe2X.dp, y = 0.dp)
                .width(80.dp)
                .height(viewModel.gap2Y.dp)
                .background(Color.Green)
        )

        // Pipe 2 - bottom rectangle
        Box(
            modifier = Modifier
                .offset(
                    x = viewModel.pipe2X.dp,
                    y = (viewModel.gap2Y + viewModel.gapSize).dp
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
        val highScore by viewModel.highScore.collectAsState()
        highScore?.let { highScore ->
            Text(
                text = "High: ${highScore.score} by ${highScore.username}",
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }

        // Instruction text - ADD THIS
        Text(
            text = "CLAP TO FLAP!",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        )
    }
}


