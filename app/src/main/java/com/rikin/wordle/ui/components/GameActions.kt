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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rikin.wordle.domain.GameAction
import com.rikin.wordle.ui.theme.Correct
import com.rikin.wordle.ui.theme.PrimaryAction
import com.rikin.wordle.ui.theme.Unused
import com.rikin.wordle.ui.theme.WordleTheme

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
                .background(color = PrimaryAction, shape = RoundedCornerShape(8.dp))
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
                .background(color = Unused, shape = RoundedCornerShape(8.dp))
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

data class IconButtonStyle(
    val icon: ImageVector,
    val iconTint: Color,
    val backgroundColor: Color,
    val textColor: Color,
)

@Composable
fun IconButton(
    text: String,
    style: IconButtonStyle,
    action: () -> Unit
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(60.dp)
                .background(
                    color = style.backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    action()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = style.icon,
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                tint = style.iconTint
            )
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = style.textColor)
    }
}

@Preview
@Composable
fun IconButtonPreview() {
    WordleTheme {
        IconButton(
            text = "Share",
            style = IconButtonStyle(
                icon = Icons.Filled.Share,
                iconTint = Color.White,
                backgroundColor = Correct,
                textColor = Color.Black
            ),
            action = {}
        )
    }
}
