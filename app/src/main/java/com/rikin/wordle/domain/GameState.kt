package com.rikin.wordle.domain

data class GameState(
    val grid: List<RowState> = listOf(
        RowState(),
        RowState(),
        RowState(),
        RowState(),
        RowState(),
        RowState()
    ),
    val keyboard: KeyboardState = KeyboardState(),
    val selectedWord: String,
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
    object Share : GameAction()
    object Retry : GameAction()
    class KeyPressed(val letter: String) : GameAction()
}

fun <T> List<T>.modify(block: MutableList<T>.() -> Unit): List<T> {
    return toMutableList().apply(block)
}

const val ROW_SIZE = 5
const val MAX_ATTEMPTS = 6
