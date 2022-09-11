package com.example.tictactoe

class Board {
    companion object {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }

    val board = Array(3) { arrayOfNulls<String>(3) }
    var aiSide = Board.CROSS
    var playerSide = Board.NOUGHT
    fun placeMove(cell: Cell, player: String) {
        board[cell.i][cell.j] = player
    }

    private val availableCells: List<Cell>
        get() {
            val cells = mutableListOf<Cell>()
            for (i in board.indices) {
                for (j in board.indices) {
                    if (board[i][j].isNullOrEmpty()) {
                        cells.add(Cell(i, j))
                    }
                }
            }
            return cells
        }
    val isGameOver: Boolean
        get() = hasNOUGHTWon() || hasCROSSWon() || availableCells.isEmpty()

    fun Tie(): Boolean = (availableCells.isEmpty())


    fun hasNOUGHTWon(): Boolean {
        if (board[0][0] == board[1][1] &&
            board[0][0] == board[2][2] &&
            board[0][0] == NOUGHT ||
            board[0][2] == board[1][1] &&
            board[0][2] == board[2][0] &&
            board[0][2] == NOUGHT
        ) {
            return true
        }

        for (i in board.indices) {
            if (
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2] &&
                board[i][0] == NOUGHT ||
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i] &&
                board[0][i] == NOUGHT
            ) {
                return true
            }
        }

        return false
    }

    fun hasCROSSWon(): Boolean {

        if (board[0][0] == board[1][1] &&
            board[0][0] == board[2][2] &&
            board[0][0] == CROSS ||
            board[0][2] == board[1][1] &&
            board[0][2] == board[2][0] &&
            board[0][2] == CROSS
        ) {
            return true
        }

        for (i in board.indices) {
            if (
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2] &&
                board[i][0] == CROSS ||
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i] &&
                board[0][i] == CROSS
            ) {
                return true
            }
        }

        return false
    }

    var computersMove: Cell? = null
    private fun checkAiWin(): Boolean {
        if (aiSide == Board.CROSS)
            return hasCROSSWon()
        else
            return hasNOUGHTWon()
    }

    private fun checkPlayerWin(): Boolean {
        if (playerSide == Board.CROSS)
            return hasCROSSWon()
        else
            return hasNOUGHTWon()
    }

    fun minimax(depth: Int, player: String, isMaximizing: Boolean): Int {
        if (checkAiWin()) return +1
        if (checkPlayerWin()) return -1
        if (availableCells.isEmpty()) return 0

        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE

        for (i in availableCells.indices) {
            val cell = availableCells[i]
            if (isMaximizing) {
                placeMove(cell, aiSide)
                val currentScore = minimax(depth + 1, playerSide, false)
                max = Math.max(currentScore, max)

                if (currentScore >= 0) {
                    if (depth == 0) computersMove = cell
                }

                if (currentScore == 1) {
                    board[cell.i][cell.j] = ""
                    break
                }

                if (i == availableCells.size - 1 && max < 0) {
                    if (depth == 0) computersMove = cell
                }

            } else if (!isMaximizing) {
                placeMove(cell, playerSide)
                val currentScore = minimax(depth + 1, aiSide, true)
                min = Math.min(currentScore, min)

                if (min == -1) {
                    board[cell.i][cell.j] = ""
                    break
                }
            }
            board[cell.i][cell.j] = ""
        }

        return if (player == aiSide) max else min
    }

}