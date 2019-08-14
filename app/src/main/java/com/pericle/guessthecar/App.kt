package com.pericle.guessthecar

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.pericle.guessthecar.database.Car
import timber.log.Timber

class App : Application() {

    var cars: List<Car> = listOf()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val db = FirebaseFirestore.getInstance()


        loadCars(db)
    }

    private fun loadCars(db: FirebaseFirestore) {
        db.collection("cars").get()
            .addOnSuccessListener {
                Timber.i("Success fetching cars!")
                if (it != null) {
                    cars = it.toObjects(Car::class.java)
                }
            }
            .addOnFailureListener {
                Timber.i(it.message, "Fetching cars failed: %s")
            }
    }

}