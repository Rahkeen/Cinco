package com.rikin.cinco.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rikin.cinco.domain.GameAction
import com.rikin.cinco.domain.GameState
import com.rikin.cinco.domain.GameStatus
import com.rikin.cinco.ui.components.GameActions
import com.rikin.cinco.ui.components.Keyboard
import com.rikin.cinco.ui.components.WordGrid
import com.rikin.cinco.ui.theme.BackgroundBlack

@Composable
fun GameScreen(state: GameState, actions: (GameAction) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundBlack)
            .padding(vertical = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        AnimatedVisibility(
            visible = state.status != GameStatus.Playing,
            enter = fadeIn() + expandVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = fadeOut() + shrinkVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                shrinkTowards = Alignment.Top
            )
        ) {
            ShareScreen(state = state, action = actions)
        }
        WordGrid(state)
        GameActions(actions)
        Keyboard(state.keyboard, actions)
    }
}
