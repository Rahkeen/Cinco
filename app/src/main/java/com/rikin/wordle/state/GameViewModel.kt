package com.rikin.wordle.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class GameState(
    val grid: List<RowState> = listOf(
        RowState(),
        RowState(),
        RowState(),
        RowState(),
        RowState(),
    ),
    val rowPosition: Int = 0,
    val status: GameStatus = GameStatus.Playing
) {
    fun updateRow(index: Int, state: RowState): List<RowState> {
        return grid.modify {
            set(index, state)
        }
    }
}

enum class GameStatus {
    Playing,
    Win,
    Lose
}

data class RowState(
    val tiles: List<TileState> = listOf(
        TileState(),
        TileState(),
        TileState(),
        TileState(),
        TileState(),
    ),
    val tilePosition: Int = 0,
    val solved: Boolean = false
) {
    fun updateTile(index: Int, state: TileState): List<TileState> {
        return tiles.modify {
            set(index, state)
        }
    }
}

data class TileState(
    val letter: String = "",
    val status: TileStatus = TileStatus.Unused
)

enum class TileStatus {
    Unused,
    Used,
    Correct,
    Incorrect,
    Misplaced
}

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
                if (state.status != GameStatus.Playing) return

                val rowPosition = state.rowPosition
                val tilePosition = state.grid[rowPosition].tilePosition

                if (tilePosition < ROW_SIZE) {
                    state = state.copy(
                        grid = state.updateRow(
                            index = rowPosition,
                            state = RowState(
                                tiles = state.grid[rowPosition].updateTile(
                                    index = tilePosition,
                                    state = TileState(
                                        letter = action.letter,
                                        status = TileStatus.Used
                                    )
                                ),
                                tilePosition = tilePosition + 1
                            )
                        )
                    )
                }
            }

            is GameAction.Delete -> {
                if (state.status != GameStatus.Playing) return

                val rowPosition = state.rowPosition
                val tilePosition = state.grid[rowPosition].tilePosition

                if (tilePosition > 0) {
                    state = state.copy(
                        grid = state.updateRow(
                            index = rowPosition,
                            state = RowState(
                                tiles = state.grid[rowPosition].updateTile(
                                    index = tilePosition - 1,
                                    state = TileState(
                                        letter = "",
                                        status = TileStatus.Unused
                                    )
                                ),
                                tilePosition = tilePosition - 1
                            )
                        )
                    )
                }
            }
            is GameAction.Submit -> {
                if (state.status != GameStatus.Playing) return

                val rowPosition = state.rowPosition
                val tilePosition = state.grid[rowPosition].tilePosition

                if (tilePosition == ROW_SIZE) {
                    // check if word is correct
                    val row = submitRow(state.grid[rowPosition])
                    val grid = state.updateRow(rowPosition, row)
                    val newRowPosition = rowPosition + 1
                    state = state.copy(
                        grid = grid,
                        rowPosition = newRowPosition,
                        status = when {
                            row.solved -> GameStatus.Win
                            newRowPosition < MAX_ATTEMPTS -> GameStatus.Playing
                            else -> GameStatus.Lose
                        }
                    )
                }
            }
        }
    }

    private fun submitRow(state: RowState): RowState {
        val lettersLeft = CORRECT_WORD.lowercase().map { "$it" }.toMutableList()
        val checkedTiles = mutableListOf<TileState>()
        var correctGuesses = 0
        state.tiles.forEachIndexed { i, tile ->
            if (CORRECT_WORD.contains(
                    tile.letter,
                    ignoreCase = true
                ) && lettersLeft.contains(tile.letter.lowercase())
            ) {
                val letter = CORRECT_WORD.substring(i, i + 1)
                if (letter == tile.letter) {
                    checkedTiles.add(tile.copy(status = TileStatus.Correct))
                    correctGuesses++
                } else {
                    checkedTiles.add(tile.copy(status = TileStatus.Misplaced))
                }
                lettersLeft.remove(letter)
            } else {
                checkedTiles.add(tile.copy(status = TileStatus.Incorrect))
            }
        }
        return state.copy(
            tiles = checkedTiles,
            solved = correctGuesses == CORRECT_WORD.length
        )
    }
}

fun <T> List<T>.modify(block: MutableList<T>.() -> Unit): List<T> {
    return toMutableList().apply(block)
}

const val ROW_SIZE = 5
const val MAX_ATTEMPTS = 5
const val CORRECT_WORD = "HELLO"
