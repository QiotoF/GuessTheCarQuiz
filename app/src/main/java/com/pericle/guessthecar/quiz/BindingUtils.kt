package com.pericle.guessthecar.quiz

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.pericle.guessthecar.database.Car

@BindingAdapter("carImage")
fun ImageView.setCarImage(car: Car?) {
    car?.let {
        this.setImageDrawable(Drawable.createFromStream(this.context.assets.open(it.imgSrc.random()), null))
    }
}