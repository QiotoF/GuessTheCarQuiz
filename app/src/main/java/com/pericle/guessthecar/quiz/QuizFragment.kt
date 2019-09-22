package com.pericle.guessthecar.quiz

import android.R
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.pericle.guessthecar.database.LevelDatabase
import com.pericle.guessthecar.databinding.FragmentQuizBinding
import timber.log.Timber


class QuizFragment : Fragment() {

    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var rewardedAd: RewardedAd
    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentQuizBinding = DataBindingUtil.inflate(
            inflater, com.pericle.guessthecar.R.layout.fragment_quiz, container, false
        )
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val arguments = QuizFragmentArgs.fromBundle(arguments!!)
        val levelDao = LevelDatabase.getInstance(application).levelDao
        val viewModelFactory =
            QuizViewModelFactory(arguments.level, /*carDao,*/ levelDao, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel::class.java)
        binding.viewModel = viewModel

        mInterstitialAd = InterstitialAd(this.context)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        rewardedAd = RewardedAd(
            this.context, "ca-app-pub-3940256099942544/5224354917"
        )
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                // Ad failed to load.
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)


        viewModel.nextBtnActive.observe(this, Observer {
            binding.btnNext.isEnabled = it ?: true
        })

        viewModel.score.observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it.toString()
        })

        viewModel.onFinish.observe(this, Observer {
            it?.let {
                if (it) {
                    if (!viewModel.rewarded && rewardedAd.isLoaded && !viewModel.noMoreQuestions) {
                        showRewardedAdDialog()
                    } else {
                        finishQuiz()
                    }
                }
            }
        })



        return binding.root
    }

    private fun showRewardedAd() {
        val adCallback = object : RewardedAdCallback() {
            override fun onUserEarnedReward(p0: RewardItem) {
                viewModel.onRewarded()
            }
        }
        rewardedAd.show(this.activity, adCallback)
    }

    private fun showRewardedAdDialog() {
        AlertDialog.Builder(context!!)
            .setTitle("Another chance?")
            .setMessage("Do you want to get an extra life by watching a video ad?")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                showRewardedAd()
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(R.string.no) { dialog, which ->
                finishQuiz()
            }
            .show()
    }

    private fun finishQuiz() {
        this.findNavController().popBackStack()
        viewModel.onFinishCompleted()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showAd()
    }

    private fun showAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Timber.d("The interstitial wasn't loaded yet.")
        }
    }
}
