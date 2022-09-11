package com.example.tictactoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.tictactoe.databinding.GameFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment : Fragment() {
    private var _binding: GameFragmentBinding? = null
    private val binding get() = _binding!!
    private var board = Board()
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }
    private var turn = Board.CROSS
    private val sharedViewModel: GameSharedViewModel by activityViewModels()
    private var crossWinCount = 0
    private var noughtWinCount = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        turn =
            if (sharedViewModel.playerSide.value == PlayerSide.CROSS) Board.CROSS else Board.NOUGHT
        board.aiSide =
            if (sharedViewModel.playerSide.value == PlayerSide.CROSS) Board.NOUGHT else Board.CROSS
        board.playerSide =
            if (sharedViewModel.playerSide.value == PlayerSide.CROSS) Board.CROSS else Board.NOUGHT
        loadBoard()

        binding.resetGameBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning")
                .setMessage("Do you want to restart the game?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(
                    R.string.yes
                ) { _, _ ->
                    board = Board()
                    mapBoardToUi()
                }
                .show()
        }
    }

    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.CROSS -> {
                        boardCells[i][j]?.setImageResource(R.drawable.x)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.NOUGHT -> {
                        boardCells[i][j]?.setImageResource(R.drawable.o)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    private fun loadBoard() {
        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(context)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                    bottomMargin = 5

                }
                boardCells[i][j]?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
                boardCells[i][j]?.setPadding(0, 10, 0, 0)
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))
                binding.layoutBoard.addView(boardCells[i][j])
            }
        }
    }

    private fun gameover(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        lifecycleScope.launch {
            delay(2000)
            board = Board()
            mapBoardToUi()
        }

    }

    inner class CellClickListener(private val i: Int, private val j: Int) : View.OnClickListener {
        override fun onClick(p0: View?) {
            if (!board.isGameOver) {
                when (sharedViewModel.gameType.value) {
                    GameType.PVP -> pvp()
                    GameType.PVA -> pva()
                    else -> throw Exception("Game Type not supported!!!")
                }

                when {
                    board.hasCROSSWon() -> {
                        crossWinCount++
                        binding.crossResultTv.text = "$crossWinCount"
                        gameover("Cross won the game")
                    }
                    board.hasNOUGHTWon() -> {
                        noughtWinCount++
                        binding.noughtResultTv.text = "$noughtWinCount"
                        gameover("Nought won the game")
                    }
                    board.Tie() -> {
                        gameover("The game equalised")
                    }
                }
            }
        }

        fun pvp() {
            board.placeMove(Cell(i, j), turn)
            mapBoardToUi()
            turn = if (turn == Board.CROSS)
                Board.NOUGHT
            else {
                Board.CROSS
            }
        }

        fun pva() {
            val cell = Cell(i, j)
            board.placeMove(cell, board.playerSide)

            board.minimax(0, board.aiSide, true)

            board.computersMove?.let {
                board.placeMove(it, board.aiSide)
            }
            mapBoardToUi()
        }
    }
}