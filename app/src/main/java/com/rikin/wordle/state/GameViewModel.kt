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
    val keyboard: KeyboardState = KeyboardState(),
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
    val status: LetterStatus = LetterStatus.Unused
)

enum class LetterStatus {
    Unused,
    Used,
    Correct,
    Incorrect,
    Misplaced
}

data class KeyboardState(
    val keyRows: List<KeyboardRowState> = listOf(
        KeyboardRowState(
            keys = listOf(
                KeyState(letter = "Q"),
                KeyState(letter = "W"),
                KeyState(letter = "E"),
                KeyState(letter = "R"),
                KeyState(letter = "T"),
                KeyState(letter = "Y"),
                KeyState(letter = "U"),
                KeyState(letter = "I"),
                KeyState(letter = "O"),
                KeyState(letter = "P"),
            )
        ),
        KeyboardRowState(
            keys = listOf(
                KeyState(letter = "A"),
                KeyState(letter = "S"),
                KeyState(letter = "D"),
                KeyState(letter = "F"),
                KeyState(letter = "G"),
                KeyState(letter = "H"),
                KeyState(letter = "J"),
                KeyState(letter = "K"),
                KeyState(letter = "L"),
            )
        ),
        KeyboardRowState(
            keys = listOf(
                KeyState(letter = "Z"),
                KeyState(letter = "X"),
                KeyState(letter = "C"),
                KeyState(letter = "V"),
                KeyState(letter = "B"),
                KeyState(letter = "N"),
                KeyState(letter = "M"),
            )
        ),
    )
)

data class KeyboardRowState(
    val keys: List<KeyState> = listOf()
)

data class KeyState(
    val letter: String = "",
    val status: LetterStatus = LetterStatus.Unused
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
                                        status = LetterStatus.Used
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
                                        status = LetterStatus.Unused
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
                    val rowToCheck = state.grid[rowPosition]
                    if (!isValidWord(rowToCheck)) {
                        return
                    }
                    val checkedRow = submitRow(rowToCheck)
                    val updatedKeyboard = updateKeyboard(checkedRow, state.keyboard)
                    val grid = state.updateRow(rowPosition, checkedRow)
                    val newRowPosition = rowPosition + 1
                    state = state.copy(
                        grid = grid,
                        keyboard = updatedKeyboard,
                        rowPosition = newRowPosition,
                        status = when {
                            checkedRow.solved -> GameStatus.Win
                            newRowPosition < MAX_ATTEMPTS -> GameStatus.Playing
                            else -> GameStatus.Lose
                        }
                    )
                }
            }
        }
    }

    private fun isValidWord(state: RowState): Boolean {
        val word = state.tiles.map { it.letter.lowercase() }.reduce { acc, s -> acc + s }
        return validWords.contains(word)
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
                if (letter == tile.letter.lowercase()) {
                    checkedTiles.add(tile.copy(status = LetterStatus.Correct))
                    correctGuesses++
                } else {
                    checkedTiles.add(tile.copy(status = LetterStatus.Misplaced))
                }
                lettersLeft.remove(letter)
            } else {
                checkedTiles.add(tile.copy(status = LetterStatus.Incorrect))
            }
        }
        return state.copy(
            tiles = checkedTiles,
            solved = correctGuesses == CORRECT_WORD.length
        )
    }

    private fun updateKeyboard(row: RowState, keyboard: KeyboardState): KeyboardState {
        val updatedRows = mutableListOf<KeyboardRowState>()
        keyboard.keyRows.forEach { keyRow ->
            val updatedKeys = mutableListOf<KeyState>()
            keyRow.keys.forEach { key ->
                val tileForKey = row.tiles.find { it.letter == key.letter }
                if (tileForKey != null) {
                    updatedKeys.add(key.copy(status = tileForKey.status))
                } else {
                    updatedKeys.add(key)
                }
            }
            updatedRows.add(KeyboardRowState(keys = updatedKeys))
        }
        return KeyboardState(
            keyRows = updatedRows
        )
    }
}

fun <T> List<T>.modify(block: MutableList<T>.() -> Unit): List<T> {
    return toMutableList().apply(block)
}

const val ROW_SIZE = 5
const val MAX_ATTEMPTS = 5
const val CORRECT_WORD = "query"
