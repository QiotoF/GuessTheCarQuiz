package com.pericle.guessthecar.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pericle.guessthecar.database.Car
import com.pericle.guessthecar.database.CarDao
import kotlinx.coroutines.*

class QuizViewModel(val database: CarDao, val app: Application) : AndroidViewModel(app) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var cars = listOf<Car>()
    private lateinit var carIterator: ListIterator<Car>


    val currentCar = MutableLiveData<Car>()

    val firstAnsw = MutableLiveData<String>()
    val secAnsw = MutableLiveData<String>()
    val thirdAnsw = MutableLiveData<String>()
    val fourthAnsw = MutableLiveData<String>()

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

    private lateinit var answers: MutableList<String>

    fun onNextClick() {
        if (carIterator.hasNext()) {
            currentCar.value = carIterator.next()

            val car = currentCar.value
            answers = cars
                .map { it.model }
                .filter { it != car?.model }
                .distinct()
                .shuffled()
                .take(3)
                .toMutableList()
            answers.add(car!!.model)
            answers.shuffle()
            firstAnsw.value = answers[0]
            secAnsw.value = answers[1]
            thirdAnsw.value = answers[2]
            fourthAnsw.value = answers[3]
        }
    }
}

