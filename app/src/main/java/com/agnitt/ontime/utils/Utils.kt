package com.agnitt.ontime.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.agnitt.ontime.R
import com.agnitt.ontime.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException

var format = { num: Int -> if (num < 10) "0$num" else "$num" }

@SuppressLint("SetTextI18n")
infix fun Int.showAsTime(activity: MainActivity) {
    val min = this / 60
    val sec = this % 60
    activity.textView.text = "${format(min)}:${format(
        sec
    )}"
}

@SuppressLint("SetTextI18n")
infix fun String.showAsState(activity: MainActivity) = when (this) {
    "Start" -> {
        activity.seekBar.isEnabled = true
        activity.button.text = "Start"
        activity.button.background = activity.getDrawable(R.color.colorTimerStart)
    }
    "Stop" -> {
        activity.seekBar.isEnabled = false
        activity.button.text = "Stop"
        activity.button.background = activity.getDrawable(R.color.colorTimerStop)
    }
    else -> null
}

infix fun String.toast(context: Context) = Toast.makeText(context, this, Toast.LENGTH_SHORT).show()

infix fun Class<*>.start(context: Context) = context.startActivity(Intent(context, this))

fun Context.share() = this.startActivity(Intent.createChooser(Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, "Hi! I use cool timer from Agnitt")
    type = "text/plain"
}, null))

fun Context.preferences() = PreferenceManager.getDefaultSharedPreferences(this)

fun String?.melody() = when (this) {
    "banjo" -> R.raw.banjo
    "bell" -> R.raw.bell
    "bells" -> R.raw.bells
    "drop" -> R.raw.drop
    "gong" -> R.raw.gong
    "krjakva" -> R.raw.krjakva
    "shot" -> R.raw.shot
    else -> R.raw.gong
}

fun SharedPreferences.getInterval(key: String, defValue: Int): Int {
    var value = 60
    try {
        value = this.getString(key, defValue.toString())?.toInt() ?: 0
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        return value
    }
}

fun Any?.isNumber(): Boolean {
    try {
        this.toString().toInt()
    } catch (nfe: NumberFormatException) {
        nfe.printStackTrace()
        return false
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return false
    }
    return true
}