package com.rikin.wordle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rikin.wordle.state.GameAction
import com.rikin.wordle.ui.theme.GreatGreen
import com.rikin.wordle.ui.theme.RadRed


@Composable
fun LetterTile(letter: String = "") {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun WordGrid(grid: List<List<String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        grid.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { letter ->
                    LetterTile(letter)
                }
            }
        }
    }
}

@Composable
fun GameActions(actions: (GameAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(60.dp)
                .background(color = GreatGreen, shape = RoundedCornerShape(8.dp))
                .clickable {
                   actions(GameAction.Submit)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(60.dp)
                .background(color = RadRed, shape = RoundedCornerShape(8.dp))
                .clickable {
                   actions(GameAction.Delete)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
fun GameActionsPreview() {
    GameActions(actions = {})
}

@Preview
@Composable
fun LetterTilePreview() {
    LetterTile()
}

@Preview
@Composable
fun LetterTileRowPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        LetterTile("H")
        LetterTile("E")
        LetterTile("L")
        LetterTile("L")
        LetterTile("O")
    }
}