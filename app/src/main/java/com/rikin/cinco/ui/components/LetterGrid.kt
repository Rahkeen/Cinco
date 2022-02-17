package com.rikin.cinco.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rikin.cinco.domain.GameState
import com.rikin.cinco.domain.LetterStatus
import com.rikin.cinco.domain.TileState
import com.rikin.cinco.ui.theme.Correct
import com.rikin.cinco.ui.theme.Incorrect
import com.rikin.cinco.ui.theme.Misplaced
import com.rikin.cinco.ui.theme.Unused

@Composable
fun LetterTile(state: TileState) {

    fun tileBackground(state: TileState): Color {
        return when (state.status) {
            LetterStatus.Unused, LetterStatus.Used -> Unused
            LetterStatus.Correct -> Correct
            LetterStatus.Incorrect -> Incorrect
            LetterStatus.Misplaced -> Misplaced
        }
    }

    fun tileTextColor(state: TileState): Color {
        return Color.White
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
fun WordGrid(state: GameState) {
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
        state.grid.forEach { row ->
            val translation = remember { Animatable(0F) }
            if (state.animateInvalidWord) {
                LaunchedEffect(key1 = state.animateInvalidWord) {
                    translation.animateTo(
                        targetValue = 0F,
                        animationSpec = keyframes {
                            durationMillis = 400
                            0F at 0
                            -50F at 50
                            0F at 100
                            50F at 150
                            0F at 200
                            -50F at 250
                            0F at 300
                            50F at 350
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.graphicsLayer(translationX = translation.value),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
    val translation = remember { Animatable(0F) }
    LaunchedEffect(key1 = true) {
        translation.animateTo(
            targetValue = 0F,
            animationSpec = keyframes {
                durationMillis = 400
                0F at 0
                -50F at 50
                0F at 100
                50F at 150
                0F at 200
                -50F at 250
                0F at 300
                50F at 350
            }
        )
    }
    Row(
        modifier = Modifier.graphicsLayer(
            translationX = translation.value
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LetterTile(TileState(0, "H", LetterStatus.Used))
        LetterTile(TileState(1, "E", LetterStatus.Used))
        LetterTile(TileState(2, "L", LetterStatus.Used))
        LetterTile(TileState(3, "L", LetterStatus.Used))
        LetterTile(TileState(4, "O", LetterStatus.Used))
    }
}