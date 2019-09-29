package com.pericle.guessthecar.quiz

import android.app.Application
import android.media.SoundPool
import android.view.View
import android.widget.Button
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.pericle.guessthecar.App
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.*
import com.pericle.guessthecar.entity.Answer
import com.pericle.guessthecar.entity.Car
import com.pericle.guessthecar.utils.setIsCorrect
import kotlinx.coroutines.*


class QuizViewModel(
    private val level: Level,
    private val levelDao: LevelDao,
    private val app: Application
) : AndroidViewModel(app) {

    companion object {
        const val NUMBER_OF_QUESTIONS = 100
        const val NUMBER_OF_LIFES = 1
    }

    private var _highScoreBet: Boolean = false
    val highScoreBet: Boolean
        get() = _highScoreBet

    private var _noMoreQuestions: Boolean = false
    val noMoreQuestions: Boolean
        get() = _noMoreQuestions
    private var _rewarded = false
    val rewarded: Boolean
        get() = _rewarded

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var cars = listOf<Car>()
    private lateinit var carIterator: ListIterator<Car>
    private val currentCar = MutableLiveData<Car>()
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _currentImage = Transformations.map(currentCar) {
        it.images.random()
    }
    val currentImage: LiveData<String>
        get() = _currentImage

    private lateinit var answers: MutableList<String?>
    private val _firstAnswer = MutableLiveData<String>()
    val firstAnswer: LiveData<String>
        get() = _firstAnswer
    private val _secAnswer = MutableLiveData<String>()
    val secAnswer: MutableLiveData<String>
        get() = _secAnswer
    private val _thirdAnswer = MutableLiveData<String>()
    val thirdAnswer: MutableLiveData<String>
        get() = _thirdAnswer
    private val _fourthAnswer = MutableLiveData<String>()
    val fourthAnswer: MutableLiveData<String>
        get() = _fourthAnswer

    private val _isFirstCorrect = MutableLiveData<Answer>()
    val isFirstCorrect: MutableLiveData<Answer>
        get() = _isFirstCorrect
    private val _isSecondCorrect = MutableLiveData<Answer>()
    val isSecondCorrect: MutableLiveData<Answer>
        get() = _isSecondCorrect
    private val _isThirdCorrect = MutableLiveData<Answer>()
    val isThirdCorrect: MutableLiveData<Answer>
        get() = _isThirdCorrect
    private val _isFourthCorrect = MutableLiveData<Answer>()
    val isFourthCorrect: MutableLiveData<Answer>
        get() = _isFourthCorrect

    private val _nextBtnText = MutableLiveData<String>()
    val nextBtnText: LiveData<String>
        get() = _nextBtnText

    private val _onFinish = MutableLiveData<Boolean>()
    val onFinish: LiveData<Boolean>
        get() = _onFinish

    private var answerClicked: Boolean = false

    private val _nextBtnActive = MutableLiveData<Boolean>()
    val nextBtnActive: LiveData<Boolean>
        get() = _nextBtnActive

    private var lifes = NUMBER_OF_LIFES
    private var onWrongAnswered = false
    private val correctSound = SoundPool.Builder().build()
    private val correctSoundId = correctSound.load(app, R.raw.correct, 0)
    private val wrongSound = SoundPool.Builder().build()
    private val wrongSoundId = wrongSound.load(app, R.raw.wrong, 0)
    private val isSoundOn = MutableLiveData<Boolean>()
    private val _soundImage = Transformations.map(isSoundOn) {
        when (it) {
            true -> R.drawable.ic_speaker
            false -> R.drawable.ic_speaker_not
        }
    }
    val soundImage: LiveData<Int>
        get() = _soundImage

    init {
        initialiseCars()
        _nextBtnText.value = app.getString(R.string.next)
        isSoundOn.value = true
    }


    private fun initialiseCars() {
        cars = (this.app as App).cars.shuffled().take(NUMBER_OF_QUESTIONS)
        carIterator = cars.listIterator()
        _score.value = 0
        onNextClick()
    }

    fun onFinishCompleted() {
        _onFinish.value = false
    }


    fun onNextClick() {
        if (!carIterator.hasNext() || lifes <= 0) {
            if (!carIterator.hasNext()) {
                _noMoreQuestions = true
            }
            if (level.highScore < score.value!!) {
                uiScope.launch {
                    updateLevel()
                }
            } else {
                _highScoreBet = true
            }
            _onFinish.value = true
        } else {
            answerClicked = false
            setCar()
            setAnswers()
            _nextBtnActive.value = false
            _score.value = _score.value?.inc()
        }
    }


    private suspend fun updateLevel() {
        level.highScore = if (lifes <= 0) _score.value!! - 1 else _score.value!!
        withContext(Dispatchers.IO) {
            levelDao.updateLevel(level)
        }
    }


    private fun setCar() {
        currentCar.value = carIterator.next()
    }

    private fun setAnswers() {
        answers = level.createAnswerList(currentCar.value, cars)
        _firstAnswer.value = answers[0]
        _secAnswer.value = answers[1]
        _thirdAnswer.value = answers[2]
        _fourthAnswer.value = answers[3]
        _isFirstCorrect.value = Answer.ANY
        _isSecondCorrect.value = Answer.ANY
        _isThirdCorrect.value = Answer.ANY
        _isFourthCorrect.value = Answer.ANY
    }

    fun onAnswerClick(view: View) {
        if (!answerClicked) {
            val btn = view as Button
            checkAnswer(btn)
            _nextBtnActive.value = true
        }
        answerClicked = true
    }

    private fun checkAnswer(btn: Button) {
        if (level.checkAnswer(currentCar.value as Car, btn.text.toString())) {
            btn.setIsCorrect(Answer.TRUE)
            onWrongAnswered = false
            playCorrectSound()
            if (!carIterator.hasNext()) {
                _nextBtnText.value = app.getString(R.string.finish)
            }
        } else {
            onWrongAnswered = true
            lifes--
            when (level.answerType(currentCar.value as Car)) {
                _firstAnswer.value -> _isFirstCorrect.value = Answer.TRUE
                _secAnswer.value -> _isSecondCorrect.value = Answer.TRUE
                _thirdAnswer.value -> _isThirdCorrect.value = Answer.TRUE
                _fourthAnswer.value -> _isFourthCorrect.value = Answer.TRUE
            }
            btn.setIsCorrect(Answer.FALSE)
            _nextBtnText.value = app.getString(R.string.finish)
            playWrongSound()
        }
    }

    private fun playCorrectSound() {
        if (isSoundOn.value!!) {
            correctSound.play(correctSoundId, 1F, 1F, 0, 0, 1F)
        }
    }

    private fun playWrongSound() {
        if (isSoundOn.value!!) {
            wrongSound.play(wrongSoundId, 1F, 1F, 0, 0, 1F)
        }
    }

    fun onSoundClick() {
        isSoundOn.value = !isSoundOn.value!!
    }

    fun onRewarded() {
        _rewarded = true
        lifes++
        _onFinish.value = false
        _nextBtnText.value = app.getString(R.string.next)
    }
}
