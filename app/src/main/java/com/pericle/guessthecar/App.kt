package com.pericle.guessthecar

import android.app.Application
import coil.Coil
import coil.api.load
import coil.request.CachePolicy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pericle.guessthecar.database.Car
import timber.log.Timber

class App : Application() {

    var cars: List<Car> = listOf()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val db = FirebaseFirestore.getInstance()
        loadCars(db)

//        val storage = FirebaseStorage.getInstance()
//        // Create a storage reference from our app
//        val storageRef = storage.reference
//        val fuck = storageRef.listAll().addOnCompleteListener {
//            val fuck2 = it.result
//            val fuck3 = fuck2?.items
//        }
//
//        var ch = storageRef.child("Lamborghini Reventon.jpg")
    }

    private fun loadCars(db: FirebaseFirestore) {
        db.collection("cars").get()
            .addOnSuccessListener {
                Timber.i("Success fetching cars!")
                if (it != null) {
                    cars = it.toObjects(Car::class.java)
                }
                prefetch()
            }
            .addOnFailureListener {
                Timber.i(it.message, "Fetching cars failed: %s")
            }
    }

    private fun prefetch() {
        for (car in cars) {
            for (img in car.images) {
                Coil.load(this, img) {
                    memoryCachePolicy(CachePolicy.DISABLED)
                }
            }
        }
    }

}
