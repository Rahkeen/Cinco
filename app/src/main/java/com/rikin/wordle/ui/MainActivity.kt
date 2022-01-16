package com.rikin.wordle.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rikin.wordle.state.GameAction
import com.rikin.wordle.state.GameState
import com.rikin.wordle.state.GameViewModel
import com.rikin.wordle.ui.components.GameActions
import com.rikin.wordle.ui.components.Keyboard
import com.rikin.wordle.ui.components.WordGrid
import com.rikin.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordleTheme {
                val gameViewModel: GameViewModel = viewModel()
                GameScreen(state = gameViewModel.state, actions = gameViewModel::send)
            }
        }
    }
}

@Composable
fun GameScreen(state: GameState, actions: (GameAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 32.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        WordGrid(state.grid)
        GameActions(actions)
        Keyboard(state.keyboard, actions)
    }
}