package com.example.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String> get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    // 단어 리스트
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val evenGameFinish: LiveData<Boolean> get() = _eventGameFinish

    init {
        Log.i("GameViewModel", "GameViewModel created!")

        resetList()
        nextWord()
        _score.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "미나리",
            "살인의 추억",
            "기생충",
            "괴물",
            "라라랜드",
            "다크나이트",
            "타짜",
            "어벤져스",
            "인셉션",
            "맘마미아",
            "악마는 프라다를 입는다",
            "설국열차",
            "해리포터",
            "인터스텔라",
            "부산행",
            "어바웃 타임",
            "신과 함께",
            "보헤미안 랩소디",
            "명량",
            "킹스맨",
            "자천자왕 엄복동"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            //gameFinished()
            _eventGameFinish.value = true
        } else {
            // 맨 앞 단어 삭제하면서 게임 진행
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /** Methods for completed events **/
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}