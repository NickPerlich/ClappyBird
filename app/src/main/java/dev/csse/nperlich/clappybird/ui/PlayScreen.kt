package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.csse.nperlich.clappybird.GameViewModel
import dev.csse.nperlich.clappybird.R
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
        Background()

        // Bird
        val birdRotation = when {
            viewModel.birdVelocity < -3f -> -30f  // Going up fast -> rotate up 30°
            viewModel.birdVelocity > 3f -> 30f    // Going down fast -> rotate down 30°
            else -> 0f                             // Neutral -> no rotation
        }

        Image(
            painter = painterResource(id = R.drawable.bird),
            contentDescription = "Bird",
            modifier = Modifier
                .offset(x = 100.dp, y = viewModel.birdY.dp)
                .size(50.dp)
                .rotate(birdRotation)
        )

        // Pipe - top
        Pipe(
            x = viewModel.pipeX.dp,
            y = 0.dp,
            height = viewModel.gapY.dp,
            isUpsideDown = false
        )

        // Pipe - bottom rectangle (gap in between)
        Pipe(
            x = viewModel.pipeX.dp,
            y = (viewModel.gapY + viewModel.gapSize).dp,
            height = 800.dp,
            isUpsideDown = true
        )

        // Pipe 2 - top
        Pipe(
            x = viewModel.pipe2X.dp,
            y = 0.dp,
            height = viewModel.gap2Y.dp,
            isUpsideDown = false
        )

        // Pipe 2 - bottom
        Pipe(
            x = viewModel.pipe2X.dp,
            y = (viewModel.gap2Y + viewModel.gapSize).dp,
            height = 800.dp,
            isUpsideDown = true
        )

        // current score
        Text(
            text = "Score: ${viewModel.currentScore}",
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(25.dp),
            color = Color.White
        )

        // high score
        val highScore by viewModel.highScore.collectAsState()
        highScore?.let { highScore ->
            Text(
                text = "High: ${highScore.score} by ${highScore.username}",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(25.dp),
                color = Color.White
            )
        }

        // Instruction text
        if (!viewModel.hasGameStarted) {
            Text(
                text = "CLAP TO FLAP!",
                fontSize = 25.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 300.dp),
                color = Color.White,
            )
        }
    }
}


