package com.pericle.guessthecar.quiz

import android.app.Application
import android.view.View
import android.widget.Button
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pericle.guessthecar.database.Car
import com.pericle.guessthecar.database.CarDao
import com.pericle.guessthecar.levels.Level
import kotlinx.coroutines.*

class QuizViewModel(
    val level: Level,
    val database: CarDao,
    val app: Application
) : AndroidViewModel(app) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var cars = listOf<Car>()
    private lateinit var carIterator: ListIterator<Car>

    val currentCar = MutableLiveData<Car>()

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

    private var answerClicked: Boolean = false

    private val _nextBtnActive = MutableLiveData<Boolean>()
    val nextBtnActive: LiveData<Boolean>
        get() = _nextBtnActive

    init {
        initialiseCars()
    }

    private fun initialiseCars() {
        uiScope.launch {
            cars = getCarsFromDatabase()
            carIterator = cars.listIterator()
            onNextClick()
        }
    }

    private suspend fun getCarsFromDatabase(): List<Car> {
        return withContext(Dispatchers.IO) {
            database.getAllCars().shuffled()
        }
    }

    fun onNextClick() {
        answerClicked = false
        if (carIterator.hasNext()) {
            setCar()
            setAnswers()
            _nextBtnActive.value = false
        }
    }

    private fun setCar() {
        currentCar.value = carIterator.next()
    }

    private fun setAnswers() {
        answers = level.createAnswerList(currentCar.value, cars)
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
        if (level.checkAnswer(currentCar.value, btn.text.toString())) {
            btn.setIsCorrect(Answer.TRUE)
        } else {
            when (level.getAnswerType(currentCar.value)) {
                this._firstAnswer.value -> isFirstCorrect.value = Answer.TRUE
                secAnswer.value -> isSecondCorrect.value = Answer.TRUE
                thirdAnswer.value -> isThirdCorrect.value = Answer.TRUE
                fourthAnswer.value -> isFourthCorrect.value = Answer.TRUE
            }
            btn.setIsCorrect(Answer.FALSE)
        }
    }
}

