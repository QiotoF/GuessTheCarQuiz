package com.pericle.guessthecar.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.CarDatabase
import com.pericle.guessthecar.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentQuizBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val dataSource = CarDatabase.getInstance(application).carDao
        val viewModelFactory = QuizViewModelFactory(dataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}
