package com.rikin.wordle.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var state by mutableStateOf(GameState(selectedWord = validWords.random()))
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
                    val checkedRow = submitRow(rowToCheck, state.selectedWord)
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
            GameAction.Retry -> {
                state = GameState(selectedWord = validWords.random())
            }
            GameAction.Share -> {
                // TODO
            }
        }
    }

    private fun isValidWord(state: RowState): Boolean {
        val word = state.tiles.map { it.letter.lowercase() }.reduce { acc, s -> acc + s }
        return validWords.contains(word)
    }

    private fun submitRow(state: RowState, selectedWord: String): RowState {
        val lettersLeft = selectedWord.map { "$it" }.toMutableList()
        val checkedTiles = mutableListOf<TileState>()
        var correctGuesses = 0
        state.tiles.forEachIndexed { i, tile ->
            if (selectedWord.contains(
                    tile.letter,
                    ignoreCase = true
                ) && lettersLeft.contains(tile.letter.lowercase())
            ) {
                val letter = selectedWord.substring(i, i + 1)
                if (letter == tile.letter.lowercase()) {
                    checkedTiles.add(tile.copy(status = LetterStatus.Correct))
                    correctGuesses++
                } else {
                    checkedTiles.add(tile.copy(status = LetterStatus.Misplaced))
                }
                lettersLeft.remove(tile.letter.lowercase())
            } else {
                checkedTiles.add(tile.copy(status = LetterStatus.Incorrect))
            }
        }
        return state.copy(
            tiles = checkedTiles,
            solved = correctGuesses == selectedWord.length
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

