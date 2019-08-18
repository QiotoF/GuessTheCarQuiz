package com.pericle.guessthecar.levels

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.MyDatabase
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

        val application = requireNotNull(this.activity).application
        val dataSource = MyDatabase.getInstance(application).levelDao
        val viewModelFactory = LevelsViewModelFactory(dataSource)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LevelsViewModel::class.java)

        val adapter = LevelsAdapter(LevelListener { level ->
            viewModel.onLevelClicked(level)
        })
        binding.levelsList.adapter = adapter

        viewModel.data.observe(this, Observer {
            it?.let {
                adapter.data = it
            }
        })

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }
}
