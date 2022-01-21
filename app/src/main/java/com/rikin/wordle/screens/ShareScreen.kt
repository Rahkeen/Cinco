package com.rikin.wordle.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rikin.wordle.domain.GameAction
import com.rikin.wordle.domain.GameState
import com.rikin.wordle.domain.GameStatus
import com.rikin.wordle.ui.components.IconButton
import com.rikin.wordle.ui.components.IconButtonStyle
import com.rikin.wordle.ui.theme.BustedBlue
import com.rikin.wordle.ui.theme.WordleTheme
import com.rikin.wordle.ui.theme.YikesYellow

@Composable
fun ShareScreen(state: GameState, action: (GameAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        fun gameText(status: GameStatus): String {
            return when (status) {
                GameStatus.Win -> "Winner!"
                GameStatus.Lose -> "Dang it!"
                GameStatus.Playing -> "Huh?"
            }
        }
        Text(
            text = gameText(state.status),
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Word: ${state.selectedWord}",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            IconButton(
                text = "Share",
                style = IconButtonStyle(
                    icon = Icons.Filled.Share,
                    iconTint = Color.White,
                    backgroundColor = BustedBlue,
                    textColor = Color.Black
                )
            ) {
                action(GameAction.Share)
            }

            IconButton(
                text = "Retry",
                style = IconButtonStyle(
                    icon = Icons.Filled.Refresh,
                    iconTint = Color.Black,
                    backgroundColor = YikesYellow,
                    textColor = Color.Black
                )
            ) {
                action(GameAction.Retry)
            }
        }
    }
}

@Preview
@Composable
fun ShareScreenPreview() {
    WordleTheme {
        ShareScreen(
            state = GameState(
                selectedWord = "Hello",
                status = GameStatus.Win
            ),
            action = {}
        )
    }
}