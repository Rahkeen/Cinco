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
import com.rikin.wordle.state.GameAction
import com.rikin.wordle.state.GameAction.KeyPressed

@Composable
fun Key(letter: String, actions: (KeyPressed) -> Unit) {
    Box(
        modifier = Modifier
            .width(30.dp)
            .aspectRatio(6.0F / 9)
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { actions(KeyPressed(letter)) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun KeyPreview() {
    Key("Q", actions = {})
}

@Composable
fun Keyboard(actions: (GameAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
            )
        ) {
            Key("Q", actions)
            Key("W", actions)
            Key("E", actions)
            Key("R", actions)
            Key("T", actions)
            Key("Y", actions)
            Key("U", actions)
            Key("I", actions)
            Key("O", actions)
            Key("P", actions)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
            )
        ) {
            Key("A", actions)
            Key("S", actions)
            Key("D", actions)
            Key("F", actions)
            Key("G", actions)
            Key("H", actions)
            Key("J", actions)
            Key("K", actions)
            Key("L", actions)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
            )
        ) {
            Key("Z", actions)
            Key("X", actions)
            Key("C", actions)
            Key("V", actions)
            Key("B", actions)
            Key("N", actions)
            Key("M", actions)
        }
    }
}

@Preview
@Composable
fun KeyboardPreview() {
    Keyboard(actions = {})
}