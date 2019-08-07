package com.pericle.guessthecar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.load.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.pericle.guessthecar.database.MyDatabase
import com.pericle.guessthecar.repository.CarRepository

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val repository = CarRepository(this.applicationContext)

        return try {
            val cars = repository.getCars()
            cars?.let {
                for (car in cars) {
                    for (img in car.images) {
                        repository.imageDownload(img)
                    }
                    repository.insertCar(car)
                }
                Result.success()
            }
            Result.retry()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}