package com.rikin.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rikin.wordle.data.ClipboardHelper
import com.rikin.wordle.domain.GameViewModel
import com.rikin.wordle.domain.GameViewModelFactory
import com.rikin.wordle.ui.screens.GameScreen
import com.rikin.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordleTheme {
                val gameViewModel: GameViewModel = viewModel(
                    factory = GameViewModelFactory(
                        clipboardHelper = ClipboardHelper(LocalClipboardManager.current)
                    )
                )
                GameScreen(
                    state = gameViewModel.state,
                    actions = gameViewModel::send
                )
            }
        }
    }
}
