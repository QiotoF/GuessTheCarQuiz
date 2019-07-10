package com.pericle.guessthecar.levels

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pericle.guessthecar.R
import com.pericle.guessthecar.databinding.FragmentLevelsBinding

class LevelsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        val binding: FragmentLevelsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_levels, container, false
        )
        binding.lifecycleOwner = this

        val data = listOf<Level>(
            Level("Models", R.drawable.car, 44),
            Level("Countries", R.drawable.car, 88),
            Level("Brands", R.drawable.car, 1)
        )
        val adapter = LevelsAdapter()
        binding.levelsList.adapter = adapter
        adapter.data = data

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }
}
