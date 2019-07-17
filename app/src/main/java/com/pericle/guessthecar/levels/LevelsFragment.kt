package com.pericle.guessthecar.levels

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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

        val data = listOf(
            BrandLevel, ModelLevel
        )

        val viewModel = ViewModelProviders.of(this).get(LevelsViewModel::class.java)

        val adapter = LevelsAdapter(LevelListener { level ->
            viewModel.onLevelClicked(level)
        })
        binding.levelsList.adapter = adapter
        adapter.data = data

        viewModel.navigateToQuiz.observe(this, Observer { level ->
            level?.let {
                this.findNavController().navigate(
                    LevelsFragmentDirections
                        .actionLevelsFragmentToQuizFragment(level)
                )
                viewModel.onQuizNavigated()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }
}
