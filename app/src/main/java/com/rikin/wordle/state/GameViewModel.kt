package com.rikin.wordle.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class GameState(
    val grid: List<List<String>> = listOf(
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
    ),
    val currentRow: Int = 0,
    val currentPosition: Int = 0,
)

sealed class GameAction {
    object Delete : GameAction()
    object Submit : GameAction()
    class KeyPressed(val letter: String) : GameAction()
}

class GameViewModel : ViewModel() {
    var state by mutableStateOf(GameState())
        private set

    fun send(action: GameAction) {
        when (action) {
            is GameAction.KeyPressed -> {
                val currentGrid = state.grid
                val currentRow = state.currentRow
                val currentPosition = state.currentPosition

                if (currentPosition in currentGrid[currentRow].indices) {
                    val newGrid = currentGrid.toMutableList().apply {
                        val newRow = get(currentRow).toMutableList().apply {
                            set(currentPosition, action.letter)
                            toList()
                        }
                        set(currentRow, newRow)
                        toList()
                    }
                    val newPosition = minOf(currentPosition+1, currentGrid[currentRow].lastIndex)

                    state = state.copy(
                        grid = newGrid,
                        currentPosition = newPosition
                    )
                }
            }
            is GameAction.Delete -> {
                val currentGrid = state.grid
                val currentRow = state.currentRow
                val currentPosition = state.currentPosition
                if (currentPosition in currentGrid[currentRow].indices) {
                    val newGrid = currentGrid.toMutableList().apply {
                        val newRow = get(currentRow).toMutableList().apply {
                            set(currentPosition, "")
                            toList()
                        }
                        set(currentRow, newRow)
                        toList()
                    }
                    val newPosition = maxOf(currentPosition-1, 0)

                    state = state.copy(
                        grid = newGrid,
                        currentPosition = newPosition
                    )
                }
            }
            is GameAction.Submit -> {}
        }
    }
}