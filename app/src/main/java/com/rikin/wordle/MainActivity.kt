package com.rikin.wordle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rikin.wordle.data.ClipboardHelper
import com.rikin.wordle.domain.GameViewModel
import com.rikin.wordle.domain.GameViewModelFactory
import com.rikin.wordle.screens.GameScreen
import com.rikin.wordle.ui.theme.Incorrect
import com.rikin.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val gameViewModel: GameViewModel = viewModel(
                factory = GameViewModelFactory(
                    clipboardHelper = ClipboardHelper(LocalClipboardManager.current)
                )
            )
            val gameState = gameViewModel.state
            val scaffoldState = rememberScaffoldState()

            Log.d("GameState", "GameState: $gameState")

            WordleTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { state ->
                        SnackbarHost(hostState = state) { data ->
                            Snackbar(
                                snackbarData = data,
                                modifier = Modifier.padding(8.dp),
                                backgroundColor = Incorrect,
                                contentColor = Color.White
                            )
                        }
                    }
                ) {
                    GameScreen(
                        state = gameState,
                        actions = gameViewModel::send
                    )

                    if (gameState.copiedWord) {
                        LaunchedEffect(scaffoldState.snackbarHostState) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Copied Result to Clipboard",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    }
                }
            }
        }
    }
}
