package com.pericle.guessthecar.quiz

import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import coil.api.load
import coil.transform.RoundedCornersTransformation
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
    car?.let {
        val imgUri = car.images.random()/*.toUri().buildUpon().scheme("https").build()*/
        this.load(imgUri) {
            crossfade(true)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
//            transformations(CircleCropTransformation())
//            transformations(BlurTransformation(this@setCarImage.context))
//            transformations(GrayscaleTransformation())
            transformations(RoundedCornersTransformation(20.0F))
        }
    }
}

@BindingAdapter("android:src")
fun ImageView.setImageViewResource(src: Int) {
    this.setImageResource(src)
}

@BindingAdapter("app:srcCompat")
fun ImageButton.setImageButtonResource(src: LiveData<Int>) {
    src.value?.let { this.setImageResource(it) }
}

@BindingAdapter("progressText")
fun TextView.setProgressText(score: Int) {
    this.text = "${score.toString()}/100"
}