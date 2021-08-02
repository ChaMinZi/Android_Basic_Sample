package com.udacity.shoestore.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailsBinding
import com.udacity.shoestore.models.Shoe

class ShoeDetailsFragment : Fragment() {
    private lateinit var binding: FragmentShoeDetailsBinding
    private val viewModel: ShoeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shoe_details, container, false
        )
        binding.lifecycleOwner = this
        binding.shoesViewModel = viewModel

        binding.shoe = viewModel.emptyShoe()

        viewModel.isSaveDetail.observe(viewLifecycleOwner, { isSave ->
            if (isSave) {
                findNavController().navigate(ShoeDetailsFragmentDirections.actionShoeDetailsToShoeList())
                viewModel.onSaveComplete()
            }
        })

        binding.btnCancel.setOnClickListener { view ->
            view.findNavController().navigate(ShoeDetailsFragmentDirections.actionShoeDetailsToShoeList())
        }

        return binding.root
    }
}