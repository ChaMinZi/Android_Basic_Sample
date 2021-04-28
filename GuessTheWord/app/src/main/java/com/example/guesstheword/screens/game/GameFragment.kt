package com.example.guesstheword.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.guesstheword.R
import com.example.guesstheword.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    // The current word
    private var word = ""

    // The current score
    private var score = 0

    // 단어 리스트
    private lateinit var wordList: MutableList<String>

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        resetList()
        nextWord()

        binding.correctButton.setOnClickListener { onCorrect() }
        binding.skipButton.setOnClickListener { onSkip() }
        updateScoreText()
        updateWordText()
        return binding.root

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
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(score)
        findNavController(this).navigate(action)
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            gameFinished()
        } else {
            // 맨 앞 단어 삭제하면서 게임 진행
            word = wordList.removeAt(0)
        }
        updateWordText()
        updateScoreText()
    }

    /** Methods for buttons presses **/

    private fun onSkip() {
        score--
        nextWord()
    }

    private fun onCorrect() {
        score++
        nextWord()
    }

    /** Methods for updating the UI **/

    private fun updateWordText() {
        binding.wordText.text = word

    }

    private fun updateScoreText() {
        binding.scoreText.text = score.toString()
    }
}