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
import com.rikin.wordle.state.RowState
import com.rikin.wordle.state.TileState
import com.rikin.wordle.state.LetterStatus
import com.rikin.wordle.ui.theme.GreatGreen
import com.rikin.wordle.ui.theme.RadRed
import com.rikin.wordle.ui.theme.YikesYellow


@Composable
fun LetterTile(state: TileState) {

    fun tileBackground(state: TileState): Color {
        return when (state.status) {
            LetterStatus.Unused, LetterStatus.Used -> Color.LightGray
            LetterStatus.Correct -> GreatGreen
            LetterStatus.Incorrect -> Color.DarkGray
            LetterStatus.Misplaced -> YikesYellow
        }
    }

    fun tileTextColor(state: TileState): Color {
        return when (state.status) {
            LetterStatus.Incorrect -> Color.White
            else -> Color.Black
        }
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = tileBackground(state),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.letter,
            style = MaterialTheme.typography.bodyLarge,
            color = tileTextColor(state)
        )
    }
}


@Composable
fun WordGrid(grid: List<RowState>) {
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
                row.tiles.forEach { tile ->
                    LetterTile(tile)
                }
            }
        }
    }
}


@Preview
@Composable
fun LetterTilePreview() {
    LetterTile(TileState(letter = "H", status = LetterStatus.Correct))
}

@Preview
@Composable
fun LetterTileRowPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        LetterTile(TileState("H", LetterStatus.Used))
        LetterTile(TileState("E", LetterStatus.Used))
        LetterTile(TileState("L", LetterStatus.Used))
        LetterTile(TileState("L", LetterStatus.Used))
        LetterTile(TileState("O", LetterStatus.Used))
    }
}