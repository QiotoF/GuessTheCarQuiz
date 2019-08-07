package com.pericle.guessthecar.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.load.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.pericle.guessthecar.database.Car
import com.pericle.guessthecar.database.MyDatabase
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CarRepository(var context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val database = MyDatabase.getInstance(context)

    fun getCars(): List<Car>? {
        var cars: List<Car>? = null
        firestore.collection("cars").get()
            .addOnSuccessListener {
                cars = it.toObjects(Car::class.java)
            }
            .addOnFailureListener {
                Timber.i(it.message, "Fetching cars failed: %s")
            }
        return cars
    }

    fun imageDownload(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                println("request failed: " + e.message)
                throw HttpException("Failed downloading an image")
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                saveImage(response, url)
            }
        })
    }

    private fun saveImage(response: Response, url: String) {
        val bitmap = BitmapFactory.decodeStream(response.body().byteStream())
        val file = File(context.getDir("car_images", Context.MODE_PRIVATE), getNameFromUrl(url))
        try {
            file.createNewFile()
            val ostream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
            ostream.flush()
            ostream.close()
        } catch (e: IOException) {
            Timber.e(e.localizedMessage)
        }
    }

    suspend fun insertCar(car: Car) {
        withContext(Dispatchers.IO) {
            val savedCar = Car(
                car.brand,
                car.country,
                car.images.map { getNameFromUrl(it) },
                car.model
            )
            database.carDao.insert(savedCar)
        }
    }

}

fun getNameFromUrl(url: String) = url.substringAfterLast("/").substringBefore("?")