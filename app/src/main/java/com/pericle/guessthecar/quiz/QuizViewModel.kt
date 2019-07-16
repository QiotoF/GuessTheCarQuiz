package com.pericle.guessthecar.quiz

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.pericle.guessthecar.database.Car
import com.pericle.guessthecar.database.CarDao
import kotlinx.coroutines.*

class QuizViewModel(val database: CarDao, val app: Application) : AndroidViewModel(app) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var cars = listOf<Car>()
    private lateinit var carIterator: ListIterator<Car>


    private val _currentCar = MutableLiveData<Car>()

    val currentImg: LiveData<Drawable> = Transformations.map(_currentCar) {
        Drawable.createFromStream(app.assets.open(it.imgSrc.random()), null)
    }


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
        if (carIterator.hasNext()) {
            _currentCar.value = carIterator.next()
        }
    }


}
