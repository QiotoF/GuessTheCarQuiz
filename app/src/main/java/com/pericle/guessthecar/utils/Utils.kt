package com.pericle.guessthecar.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.Level


fun shareApp(activity: Activity) {
    try {
        val appPackageName = activity.packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT, activity.resources.getString(R.string.share)
                    + " https://play.google.com/store/apps/details?id=" + appPackageName
        )
        sendIntent.type = "text/plain"
        activity.startActivity(sendIntent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun rateApp(activity: Activity) {
    try {
        activity.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + activity.packageName)
            )
        )
    } catch (e: ActivityNotFoundException) {
        activity.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + activity.packageName)
            )
        )
    }
}

fun openPrivacyPolicy(activity: Activity) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pericle.ucoz.net/index/guess-the-car-quiz/0-4"))
    activity.startActivity(browserIntent)
}

operator fun Level.compareTo(score: Int): Int {
    return highScore.compareTo(score)
}