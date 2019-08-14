package com.pericle.guessthecar.quiz

import android.app.Application
import android.view.View
import android.widget.Button
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pericle.guessthecar.App
import com.pericle.guessthecar.database.*
import kotlinx.coroutines.*


class QuizViewModel(
    val level: Level,
    val carDao: CarDao,
    val levelDao: LevelDao,
    val app: Application
) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var cars = listOf<Car>()
    private lateinit var carIterator: ListIterator<Car>
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

    private var answerClicked: Boolean = false

    private val _nextBtnActive = MutableLiveData<Boolean>()
    val nextBtnActive: LiveData<Boolean>
        get() = _nextBtnActive

    init {
        initialiseCars()
    }


    private fun initialiseCars() {

        cars = (this.app as App).cars.shuffled()
        carIterator = cars.listIterator()
        _score.value = 0
        onNextClick()

//        db.collection("cars").get()
//            .addOnSuccessListener {
//                Timber.i("Success fetching cars!")
//                if (it != null) {
//                    cars = it.toObjects(Car::class.java)
//                    carIterator = cars.listIterator()
//                    _score.value = 0
//                    onNextClick()
//                }
//            }
//            .addOnFailureListener {
//                Timber.i(it.message, "Fetching cars failed: %s")
//            }

//        db.collection("cars")
//            .addSnapshotListener { value, e ->
//                if (e != null) {
//                    return@addSnapshotListener
//                }
//                cars = value!!.toObjects(Car::class.java)
//                uiScope.launch {
////                    for (car in cars) {
////                        imageDownload(car.images[0])
////                    }
//                    carIterator = cars.listIterator()
//                    _score.value = 0
//                    onNextClick()
//                }
//            }


    }

    fun onNextClick() {
        if (carIterator.hasNext()) {
            answerClicked = false
            setCar()
            setAnswers()
            _nextBtnActive.value = false
            _score.value = _score.value?.inc()
        } else {
            if (score.value!! > level.highScore) {
                uiScope.launch {
                    updateLevel()
                }
            }
        }
    }

    private suspend fun updateLevel() {
        level.highScore = _score.value!!
        withContext(Dispatchers.IO) {
            levelDao.insert(level)
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
            when (level.answerType(currentCar.value)) {
                this._firstAnswer.value -> isFirstCorrect.value = Answer.TRUE
                secAnswer.value -> isSecondCorrect.value = Answer.TRUE
                thirdAnswer.value -> isThirdCorrect.value = Answer.TRUE
                fourthAnswer.value -> isFourthCorrect.value = Answer.TRUE
            }
            btn.setIsCorrect(Answer.FALSE)
        }
    }
}

