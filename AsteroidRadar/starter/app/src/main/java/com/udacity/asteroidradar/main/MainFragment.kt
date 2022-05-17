package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        binding.statusLoadingWheel.progress = 30

        setUpAsteroidRecyclerView()

        return binding.root
    }

    private fun setUpAsteroidRecyclerView() {
        val recyclerAdapter = MainAdapter(MainAdapter.AsteroidClickListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter = recyclerAdapter

        // update Recycler View
        viewModel.asteroidList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.statusLoadingWheel.visibility = View.GONE

            recyclerAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week -> viewModel.getWeekAsteroid()
            R.id.show_saved -> viewModel.getSavedAsteroid()
            R.id.show_today -> viewModel.getTodayAsteroid()
        }
        return true
    }
}
