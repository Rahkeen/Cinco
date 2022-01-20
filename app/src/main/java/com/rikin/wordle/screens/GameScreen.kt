package com.rikin.wordle.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rikin.wordle.domain.GameAction
import com.rikin.wordle.domain.GameState
import com.rikin.wordle.domain.GameStatus
import com.rikin.wordle.ui.components.GameActions
import com.rikin.wordle.ui.components.Keyboard
import com.rikin.wordle.ui.components.WordGrid

@Composable
fun GameScreen(state: GameState, actions: (GameAction) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(
            space = 32.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        AnimatedVisibility(visible = state.status != GameStatus.Playing) {
            ShareScreen(state = state, action = actions)
        }
        WordGrid(state.grid)
        GameActions(actions)
        Keyboard(state.keyboard, actions)
    }
}
