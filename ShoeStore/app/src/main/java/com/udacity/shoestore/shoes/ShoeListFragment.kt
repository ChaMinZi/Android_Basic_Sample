package com.udacity.shoestore.shoes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.databinding.LayoutShoeItemBinding
import com.udacity.shoestore.details.ShoeViewModel
import com.udacity.shoestore.models.Shoe

class ShoeListFragment : Fragment() {
    private lateinit var binding: FragmentShoeListBinding
    private val viewModel: ShoeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shoe_list, container, false
        )
        val shoesLayout = binding.root.findViewById<LinearLayout>(R.id.ll_shoes)
        binding.lifecycleOwner = this

        viewModel.shoes.observe(viewLifecycleOwner, { shoes ->
            if (shoes.isNotEmpty()) {
                shoes.forEach { shoe ->
                    val shoeLayout = DataBindingUtil.inflate<LayoutShoeItemBinding>(
                        inflater, R.layout.layout_shoe_item, container, false
                    )
                    shoeLayout.shoe = shoe
                    shoesLayout.addView(shoeLayout.root)
                }
            }
        })

        binding.fbDetailShoe.setOnClickListener { view ->
            view.findNavController().navigate(ShoeListFragmentDirections.actionShoeListToShoeDetails())
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                findNavController().navigate(ShoeListFragmentDirections.actionShoeListToLogin())
                true
            }
            else -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                        || super.onOptionsItemSelected(item)
            }
        }
    }
}