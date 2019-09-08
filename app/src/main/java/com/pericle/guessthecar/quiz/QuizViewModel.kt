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
import com.pericle.guessthecar.utils.compareTo
import com.pericle.guessthecar.database.*
import com.pericle.guessthecar.entity.*
import com.pericle.guessthecar.utils.setIsCorrect
import kotlinx.coroutines.*


class QuizViewModel(
    val level: Level,
    val levelDao: LevelDao,
    val app: Application
) : AndroidViewModel(app) {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var cars = listOf<QuizItem>()
    private lateinit var carIterator: ListIterator<QuizItem>
    val currentCar = MutableLiveData<Car>()
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private lateinit var answers: MutableList<String?>
    private val _firstAnswer = MutableLiveData<String>()
    val firstAnswer: LiveData<String>
        get() = _firstAnswer
    val secAnswer = MutableLiveData<String>()
    val thirdAnswer = MutableLiveData<String>()
    val fourthAnswer = MutableLiveData<String>()

    val isFirstCorrect = MutableLiveData<Answer>()
    val isSecondCorrect = MutableLiveData<Answer>()
    val isThirdCorrect = MutableLiveData<Answer>()
    val isFourthCorrect = MutableLiveData<Answer>()

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
        _nextBtnText.value = "Next"
        isSoundOn.value = true
    }


    private fun initialiseCars() {
        cars = (this.app as App).cars.shuffled()
        carIterator = cars.listIterator()
        _score.value = 0
        onNextClick()
    }

    fun onFinishCompleted() {
        _onFinish.value = false
    }


    fun onNextClick() {
        if (!carIterator.hasNext() || onWrongAnswered) {
            if (level < score.value!!) {
                uiScope.launch {
                    updateLevel()
                }
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
        level.highScore = if (onWrongAnswered) _score.value!! - 1 else _score.value!!
        withContext(Dispatchers.IO) {
            levelDao.updateLevel(level)
        }
    }


    private fun setCar() {
        currentCar.value = carIterator.next() as Car
    }

    private fun setAnswers() {
        answers = level.createAnswerList(currentCar.value, cars as List<Car>)
        this._firstAnswer.value = answers[0]
        secAnswer.value = answers[1]
        thirdAnswer.value = answers[2]
        fourthAnswer.value = answers[3]
        isFirstCorrect.value = Answer.ANY
        isSecondCorrect.value = Answer.ANY
        isThirdCorrect.value = Answer.ANY
        isFourthCorrect.value = Answer.ANY
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
        } else {
            onWrongAnswered = true
            when (level.answerType(currentCar.value as Car)) {
                this._firstAnswer.value -> isFirstCorrect.value = Answer.TRUE
                secAnswer.value -> isSecondCorrect.value = Answer.TRUE
                thirdAnswer.value -> isThirdCorrect.value = Answer.TRUE
                fourthAnswer.value -> isFourthCorrect.value = Answer.TRUE
            }
            btn.setIsCorrect(Answer.FALSE)
            _nextBtnText.value = "Finish"
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
}
