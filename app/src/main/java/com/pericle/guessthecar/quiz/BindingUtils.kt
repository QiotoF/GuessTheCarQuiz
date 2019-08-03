package com.pericle.guessthecar.quiz

import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.Car
import com.squareup.picasso.Picasso
import java.io.File

//@BindingAdapter("carImage")
//fun ImageView.setCarImage(car: Car?) {
//    car?.let {
//        this.setImageDrawable(Drawable.createFromStream(this.context.assets.open(it.images.random()), null))
//    }
//}

@BindingAdapter("isCorrect")
fun Button.setIsCorrect(answer: Answer?) {
    this.setBackgroundResource(when(answer) {
        Answer.TRUE -> R.drawable.rectangle_quiz_green_normal
        Answer.FALSE -> R.drawable.rectangle_quiz_red_normal
        Answer.ANY -> R.drawable.rectangle_white_normal
        else -> R.drawable.rectangle_white_normal
    })
}


fun adapt(url: String): String {
    val path = Environment.getExternalStorageDirectory().path + "/" + "fuck.jpg"
    val file = File(path)
    return if (file.exists()) {
        path
    } else {
        url
    }
}

@BindingAdapter("carImage")
fun ImageView.setCarImage(car: Car?) {
    car?.let {
        val imgUri = car.images.random()/*.toUri().buildUpon().scheme("https").build()*/
//        Glide.with(context)
//            .load(imgUri)
//            .apply(
//                RequestOptions()
//                .placeholder(R.drawable.loading_animation)
//                .error(R.drawable.ic_broken_image))
//            .into(this)
        val file = File(Environment.getExternalStorageDirectory().path + "/" + "fuck.jpg")
        Picasso.get()
            .load(file)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(this)
    }
}