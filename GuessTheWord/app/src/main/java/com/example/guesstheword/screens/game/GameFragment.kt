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

        // Data Binding과 ViewModel 연결하기
        binding.gameViewModel = viewModel

        // Data binding이 LiveData를 관찰하여 자동으로 업데이트하게 해줌
        binding.lifecycleOwner = this

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
     * onSaveInstanceState 를 이용하는 것도 좋은 방법이지만
     * LifeCycle 의 라이브러리를 이용하는 것이 더 좋다.
     **/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("GameFragment", "onSaveInstanceState called")
    }
}