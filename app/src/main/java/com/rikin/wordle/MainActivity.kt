package com.rikin.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rikin.wordle.domain.GameViewModel
import com.rikin.wordle.ui.screens.GameScreen
import com.rikin.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordleTheme {
                val gameViewModel: GameViewModel = viewModel()
                GameScreen(
                    state = gameViewModel.state,
                    actions = gameViewModel::send
                )
            }
        }
    }
}
