package com.example.tictactoe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class GameType {
    PVP, PVA
}

enum class PlayerSide {
    NOUGHT, CROSS
}

class GameSharedViewModel : ViewModel() {
    private val _gameType: MutableStateFlow<GameType> = MutableStateFlow(GameType.PVP)
     var gameType: StateFlow<GameType> = _gameType


    private val _playerSide = MutableStateFlow<PlayerSide>(PlayerSide.CROSS)
     val playerSide: StateFlow<PlayerSide> = _playerSide


    fun setGameType(type: GameType) {
        _gameType.value = type
    }

    fun setPlaySide(side: PlayerSide) {
        _playerSide.value = side
    }
}