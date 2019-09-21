package com.pericle.guessthecar

import android.app.Application
import android.content.Context
import coil.Coil
import coil.api.load
import coil.request.CachePolicy
import com.google.firebase.firestore.FirebaseFirestore
import com.pericle.guessthecar.entity.Car
import kotlinx.coroutines.*
import timber.log.Timber

class App : Application() {



    var cars: List<Car> = listOf()
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        mContext = this
        Timber.plant(Timber.DebugTree())
        val db = FirebaseFirestore.getInstance()
//        uiScope.launch {
            loadCars(db)
//        }
    }

    companion object{
        private lateinit var mContext: Context
        fun getContext() = mContext
    }

    private /*suspend*/ fun loadCars(db: FirebaseFirestore) {
//        withContext(Dispatchers.IO) {
            db.collection("cars").get()
                .addOnSuccessListener {
                    Timber.i("Success fetching cars!")
                    if (it != null) {
                        cars = it.toObjects(Car::class.java)
                    }
//                    prefetch()
                }
                .addOnFailureListener {
                    Timber.i(it.message, "Fetching cars failed: %s")
                }
//        }

    }

//    private fun prefetch() {
//        for (car in cars) {
//            for (img in car.images) {
//                Coil.load(this, img) {
//                    memoryCachePolicy(CachePolicy.DISABLED)
//                }
//            }
//        }
//    }

}
