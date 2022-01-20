package com.rikin.wordle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rikin.wordle.domain.GameAction
import com.rikin.wordle.domain.GameAction.KeyPressed
import com.rikin.wordle.domain.KeyState
import com.rikin.wordle.domain.KeyboardState
import com.rikin.wordle.domain.LetterStatus
import com.rikin.wordle.ui.theme.GreatGreen
import com.rikin.wordle.ui.theme.YikesYellow

@Composable
fun Key(state: KeyState, actions: (KeyPressed) -> Unit) {

    fun backgroundColor(status: LetterStatus): Color {
        return when (status) {
            LetterStatus.Unused, LetterStatus.Used -> Color.LightGray
            LetterStatus.Correct -> GreatGreen
            LetterStatus.Incorrect -> Color.DarkGray
            LetterStatus.Misplaced -> YikesYellow
        }
    }

    fun textColor(status: LetterStatus): Color {
        return when (status) {
            LetterStatus.Incorrect -> Color.White
            else -> Color.Black
        }
    }

    Box(
        modifier = Modifier
            .width(30.dp)
            .aspectRatio(6.0F / 9)
            .background(
                color = backgroundColor(state.status),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { actions(KeyPressed(state.letter)) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.letter,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor(state.status)
        )
    }
}

@Preview
@Composable
fun KeyPreview() {
    Key(KeyState("Q"), actions = {})
}

@Composable
fun Keyboard(state: KeyboardState, actions: (GameAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.keyRows.forEach { keyRow ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                )
            ) {
                keyRow.keys.forEach { key ->
                    Key(
                        state = key,
                        actions = actions
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun KeyboardPreview() {
    Keyboard(state = KeyboardState(), actions = {})
}