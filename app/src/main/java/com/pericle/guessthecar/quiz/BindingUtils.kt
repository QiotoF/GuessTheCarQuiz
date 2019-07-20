package com.pericle.guessthecar.quiz

import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.Car

@BindingAdapter("carImage")
fun ImageView.setCarImage(car: Car?) {
    car?.let {
        this.setImageDrawable(Drawable.createFromStream(this.context.assets.open(it.imgSrc.random()), null))
    }
}

@BindingAdapter("isCorrect")
fun Button.setIsCorrect(answer: Answer?) {
    this.setBackgroundResource(when(answer) {
        Answer.TRUE -> R.drawable.rectangle_quiz_green_normal
        Answer.FALSE -> R.drawable.rectangle_quiz_red_normal
        Answer.ANY -> R.drawable.rectangle_white_normal
        else -> R.drawable.rectangle_white_normal
    })
}