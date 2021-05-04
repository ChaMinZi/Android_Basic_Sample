package com.example.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.guesstheword.R
import com.example.guesstheword.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

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

        // viewModel 참조하기
        Log.i("GameFragment", "Called ViewModelProvider!")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
        }

        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, { newWord ->
            binding.wordText.text = newWord

        })

        viewModel.currentTime.observe(viewLifecycleOwner, { newTime ->
            binding.remainTimeText.text = DateUtils.formatElapsedTime(newTime)
        })

        viewModel.evenGameFinish.observe(viewLifecycleOwner, { isFinished ->
            if (isFinished) {
                // viewModel.score.value 가 null 이면 0을 전달
                val currentScore = viewModel.score.value ?: 0
                val action = GameFragmentDirections.actionGameToScore(currentScore)
                findNavController(this).navigate(action)
                viewModel.onGameFinishComplete()
            }
        })

        return binding.root

    }

    /**
     * Called when the game is finished
     */
//    fun gameFinished() {
//        // viewModel.score.value 가 null 이면 0을 전달
//        val currentScore = viewModel.score.value ?: 0
//        val action = GameFragmentDirections.actionGameToScore(currentScore)
//        findNavController(this).navigate(action)
//    }

    /**
     * onSaveInstanceState 를 이용하는 것도 좋은 방법이지만
     * LifeCycle 의 라이브러리를 이용하는 것이 더 좋다.
     **/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("GameFragment", "onSaveInstanceState called")
    }
}