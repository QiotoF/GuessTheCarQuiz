package com.pericle.guessthecar.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.MyDatabase
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
        val arguments = QuizFragmentArgs.fromBundle(arguments!!)
//        val carDao = MyDatabase.getInstance(application).carDao
        val levelDao = MyDatabase.getInstance(application).levelDao
        val viewModelFactory = QuizViewModelFactory(arguments.level, /*carDao,*/ levelDao, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.nextBtnActive.observe(this, Observer {
            binding.btnNext.isEnabled = it ?: true
        })

        viewModel.score.observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it.toString()
        })

        viewModel.onFinish.observe(this, Observer {
            it?.let {
                if (it) {
                    this.findNavController().popBackStack()
                    viewModel.onFinishCompleted()
                }
            }
        })


        return binding.root
    }
}
