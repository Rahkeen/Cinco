package com.rikin.wordle.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rikin.wordle.data.ClipboardHelper

class GameViewModel(private val clipboardHelper: ClipboardHelper) : ViewModel() {
    var state by mutableStateOf(GameState(selectedWord = commonWords.random()))
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
                                        index = tilePosition,
                                        letter = action.letter,
                                        status = LetterStatus.Used
                                    )
                                ),
                                tilePosition = tilePosition + 1
                            )
                        ),
                        animateInvalidWord = false
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
                                        index = tilePosition - 1,
                                        letter = "",
                                        status = LetterStatus.Unused
                                    )
                                ),
                                tilePosition = tilePosition - 1
                            )
                        ),
                        animateInvalidWord = false
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
                        state = state.copy(animateInvalidWord = true)
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
                        animateInvalidWord = false,
                        status = when {
                            checkedRow.solved -> GameStatus.Win
                            newRowPosition < MAX_ATTEMPTS -> GameStatus.Playing
                            else -> GameStatus.Lose
                        }
                    )
                }
            }
            GameAction.Retry -> {
                state = GameState(selectedWord = commonWords.random())
            }
            GameAction.Share -> {
                val title = when (state.status) {
                    GameStatus.Win -> "Win: ${state.rowPosition}/${state.grid.size}"
                    else -> "Lose"
                }
                val emojiGrid = state.grid
                    .filter { row -> row.tiles[0].status != LetterStatus.Unused }
                    .map(this::convertRowToEmojiString)
                    .reduce { grid, row -> "$grid\n$row" }

                clipboardHelper.copy("$title\n\n$emojiGrid")
            }
        }
    }

    private fun convertRowToEmojiString(row: RowState): String {
        return row.tiles
            .map { tile ->
                when (tile.status) {
                    LetterStatus.Correct -> "\uD83D\uDFE9"
                    LetterStatus.Misplaced -> "\uD83D\uDFE8"
                    else -> "⬜"
                }
            }
            .reduce { acc, s -> acc + s }
    }

    private fun isValidWord(state: RowState): Boolean {
        val word = state.tiles.map { it.letter.lowercase() }.reduce { acc, s -> acc + s }
        return allWords.contains(word)
    }

    private fun submitRow(state: RowState, selectedWord: String): RowState {
        val lettersLeft = selectedWord.map { "$it" }.toMutableList()
        val checkedTiles = mutableListOf(
            TileState(),
            TileState(),
            TileState(),
            TileState(),
            TileState()
        )
        val checkedIndices = BooleanArray(5)
        var correctGuesses = 0
        // first pass for correct letters
        state.tiles.forEachIndexed { i, tile ->
            val letter = tile.letter.lowercase()
            if (selectedWord.substring(i, i + 1) == letter) {
                checkedTiles[i] = tile.copy(status = LetterStatus.Correct)
                checkedIndices[i] = true
                lettersLeft.remove(letter)
                correctGuesses++
            }
        }

        // second pass for other letters, i know it's gross for now don't @ me
        state.tiles.forEachIndexed { i, tile ->
            if (!checkedIndices[i]) {
                if (selectedWord.contains(tile.letter, true) &&
                    lettersLeft.contains(tile.letter.lowercase())
                ) {
                    checkedTiles[i] = tile.copy(status = LetterStatus.Misplaced)
                    lettersLeft.remove(tile.letter.lowercase())
                } else {
                    checkedTiles[i] = tile.copy(status = LetterStatus.Incorrect)
                }
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

class GameViewModelFactory(
    private val clipboardHelper: ClipboardHelper
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(clipboardHelper) as T
    }
}