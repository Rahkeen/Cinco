package com.rikin.wordle.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rikin.wordle.domain.RowState
import com.rikin.wordle.domain.TileState
import com.rikin.wordle.domain.LetterStatus
import com.rikin.wordle.ui.theme.GreatGreen
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

    val background by animateColorAsState(
        targetValue = tileBackground(state),
        animationSpec = if (state.status == LetterStatus.Unused) {
            snap()
        } else {
            tween(delayMillis = 200 * state.index)
        }
    )

    val textColor by animateColorAsState(
        targetValue = tileTextColor(state),
        animationSpec = tween(delayMillis = 200 * state.index)
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = background,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.letter,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
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
        LetterTile(TileState(0, "H", LetterStatus.Used))
        LetterTile(TileState(1, "E", LetterStatus.Used))
        LetterTile(TileState(2, "L", LetterStatus.Used))
        LetterTile(TileState(3, "L", LetterStatus.Used))
        LetterTile(TileState(4, "O", LetterStatus.Used))
    }
}