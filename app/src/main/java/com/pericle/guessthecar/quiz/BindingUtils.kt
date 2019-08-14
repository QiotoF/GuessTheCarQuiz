package com.pericle.guessthecar.quiz

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.Car

@BindingAdapter("isCorrect")
fun Button.setIsCorrect(answer: Answer?) {
    this.setBackgroundResource(
        when (answer) {
            Answer.TRUE -> R.drawable.rectangle_quiz_green_normal
            Answer.FALSE -> R.drawable.rectangle_quiz_red_normal
            Answer.ANY -> R.drawable.rectangle_white_normal
            else -> R.drawable.rectangle_white_normal
        }
    )
}

@BindingAdapter("carImage")
fun ImageView.setCarImage(car: Car?) {
    car?.let{
        val imgUri = car.images.random()/*.toUri().buildUpon().scheme("https").build()*/
        this.load(imgUri) {
            crossfade(true)
            placeholder(R.drawable.loading_animation)
//            transformations(CircleCropTransformation())
//            transformations(BlurTransformation(this@setCarImage.context))
//            transformations(GrayscaleTransformation())
            transformations(RoundedCornersTransformation(20.0F))
        }
    }
}