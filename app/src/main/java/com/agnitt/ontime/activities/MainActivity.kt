package com.agnitt.ontime.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.agnitt.ontime.R
import com.agnitt.ontime.utils.*
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(),
    SeekBar.OnSeekBarChangeListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var timer: CountDownTimer
    var defaultInterval: Int = 0
    var time: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        seekBar.setOnSeekBarChangeListener(this)
        preferences().registerOnSharedPreferenceChangeListener(this)
        setIntervalFromSharedPreferences(preferences())
    }

    fun timer(v: View) {
        if ((v as Button).text == "Start") {
            time = seekBar.progress * 1000

            if (time == 0) return "Пожалуйста, установите таймер" toast this
            "Stop" showAsState this@MainActivity

            timer = object : CountDownTimer(
                time.toLong(), 1000
            ) {

                override fun onTick(millisUntilFinished: Long) {
                    time = (millisUntilFinished / 1000).toInt()
                    seekBar.progress = time
                    time showAsTime this@MainActivity
                }

                override fun onFinish() {
                    var pref = this@MainActivity.preferences()
                    if (pref.getBoolean("enable_sound", true)) MediaPlayer.create(
                        this@MainActivity, pref.getString("timer_melody", "gong").melody()
                    ).start()
                    "Start" showAsState this@MainActivity
                }
            }
            timer.start()
        } else {
            "Start" showAsState this@MainActivity
            timer.cancel()
        }
    }

    fun setIntervalFromSharedPreferences(sharedPreferences: SharedPreferences) {
        defaultInterval = sharedPreferences.getInterval("default_interval", 60)
        seekBar.progress = defaultInterval
        defaultInterval showAsTime this@MainActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        preferences().unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        progress showAsTime this@MainActivity
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.timer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> SettingsActivity::class.java start this
            R.id.action_share -> share()
            R.id.action_about -> AboutActivity::class.java start this
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "default_interval") setIntervalFromSharedPreferences(preferences())
    }
}
