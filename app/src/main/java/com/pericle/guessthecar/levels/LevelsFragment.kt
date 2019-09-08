package com.pericle.guessthecar.levels

import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.LevelDatabase
import com.pericle.guessthecar.databinding.FragmentLevelsBinding
import com.pericle.guessthecar.utils.openPrivacyPolicy
import com.pericle.guessthecar.utils.rateApp
import com.pericle.guessthecar.utils.shareApp

class LevelsFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.rate -> rateApp(activity!!)
            R.id.share -> shareApp(activity!!)
            R.id.privacy_policy -> openPrivacyPolicy(activity!!)
            R.id.exit -> activity?.finish()
            R.id.settingsFragment -> navigateToSettings()
        }
        return true
    }

    private fun navigateToSettings() {
        this.findNavController()
            .navigate(LevelsFragmentDirections.actionLevelsFragmentToSettingsFragment())
    }

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
        val dataSource = LevelDatabase.getInstance(application).levelDao
        val viewModelFactory = LevelsViewModelFactory(dataSource)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LevelsViewModel::class.java)

        activity?.findViewById<NavigationView>(R.id.nav_view)
            ?.setNavigationItemSelectedListener(this)
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


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item!!,
            view!!.findNavController()
        )
                || super.onOptionsItemSelected(item)
    }
}
