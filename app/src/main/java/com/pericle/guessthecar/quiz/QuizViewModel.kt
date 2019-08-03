package com.pericle.guessthecar.quiz

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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


//
//    fun imageDownload(urls: List<String>) {
//        for (url in urls) {
//            Picasso.get()
//                .load(url)
//                .placeholder(R.drawable.loading_animation)
//                .into(getTarget(url))
//        }
//    }
//
//
//    private fun getTarget(url: String): Target {
//        return object : Target {
//
//            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
//                Thread(Runnable {
//                    val file = File(Environment.getExternalStorageDirectory().path + "/" + url)
//                    try {
//                        file.createNewFile()
//                        val ostream = FileOutputStream(file)
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
//                        ostream.flush()
//                        ostream.close()
//                    } catch (e: IOException) {
//                        Timber.e(e.localizedMessage)
//                    }
//                }).start()
//
//            }
//
//            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
//
//            }
//
//            override fun onPrepareLoad(placeHolderDrawable: Drawable) {
//
//            }
//        }
//
//    }

//    private suspend fun imageDownload(urls: List<String>) {
//        withContext(Dispatchers.IO) {
//            for (urlString in urls) {
//                val url = URL(urlString)
//                val input = url.openStream()
//                try {
//                    //The sdcard directory e.g. '/sdcard' can be used directly, or
//                    //more safely abstracted with getExternalStorageDirectory()
//                    val storagePath = Environment.getExternalStorageDirectory().absolutePath
//                    val output = FileOutputStream("$storagePath/$urlString")
//                    try {
//                        val buffer = ByteArray(1024)
//                        var bytesRead = 0
//                        bytesRead = (input.read(buffer, 0, buffer.size))
//                        while (bytesRead >= 0) {
//                            output.write(buffer, 0, bytesRead)
//                            bytesRead = (input.read(buffer, 0, buffer.size))
//                        }
//                    } finally {
//                        output.close()
//                    }
//                } finally {
//                    input.close()
//                }
//            }
//        }
//    }

    fun imageDownload(url: String) {
        val target = getTarget(url)
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.loading_animation)
            .into(target)
    }

    //target to save
    private fun getTarget(url: String): Target {
        return object : Target {
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                Thread(Runnable {
//                    val file = File(Environment.getExternalStorageDirectory().path + "/" + "fuck")

                    val file = File(app.getDir("car_images", Context.MODE_PRIVATE), "fuck")
                    try {
                        file.createNewFile()
                        val ostream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                        ostream.flush()
                        ostream.close()
                    } catch (e: IOException) {
                        Timber.e(e.getLocalizedMessage())
                    }
                }).start()

            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {

            }
        }
    }

    private fun setDummyCars() {
        cars = listOf(
            Car(brand = "Fuck", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "Fuck", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "Fuck", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "gsda", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "vds", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "ewfr", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "Fuck", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "Fuck", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "asd", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "fsd", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "das", country = "fuck", images = listOf("fuck"), model = "fuck"),
            Car(brand = "Fuck", country = "fuck", images = listOf("fuck"), model = "fuck")

        )

        carIterator = cars.listIterator()
        _score.value = 0
        onNextClick()
    }


    private fun initialiseCars() {
//        db.collection("cars").get()
//            .addOnSuccessListener {
//                Timber.i("Success fetching cars!")
//                if (it != null) {
//                    cars = it.toObjects(Car::class.java)
//                    uiScope.launch {
//                        for (car in cars) {
//                            imageDownload(car.images[0])
//                        }
//                        carIterator = cars.listIterator()
//                        _score.value = 0
//                        onNextClick()
//                    }
//                }
//            }
//            .addOnFailureListener {
//                Timber.i(it.message, "Fetching cars failed: %s")
//            }

        db.collection("cars")
            .addSnapshotListener{value, e->
                if (e != null) {
                    return@addSnapshotListener
                }
                cars = value!!.toObjects(Car::class.java)
                uiScope.launch {
                    for (car in cars) {
                        imageDownload(car.images[0])
                    }
                    carIterator = cars.listIterator()
                    _score.value = 0
                    onNextClick()
                }
            }

//        setDummyCars()

//        uiScope.launch {
//            cars = getCarsFromDatabase()
//            carIterator = cars.listIterator()
//            _score.value = 0
//            onNextClick()
//        }
    }

//    private suspend fun getCarsFromDatabase(): List<Car> {
//        return withContext(Dispatchers.IO) {
//            carDao.getAllCars().shuffled()
//        }
//    }

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

