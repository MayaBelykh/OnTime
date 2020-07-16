package com.agnitt.ontime

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.agnitt.ontime.utils.getInterval
import com.agnitt.ontime.utils.isNumber

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    Preference.OnPreferenceChangeListener {

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean =
        if (preference?.key == "default_interval") newValue.isNumber() else false

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        var preference: Preference? = findPreference(key as CharSequence)
        if (preference is ListPreference) {
            var value = sharedPreferences?.getString(preference.key, "")!!
            setPreferenceLabel(preference, value)
        } else if (preference is EditTextPreference) {
            var value = sharedPreferences?.getInterval(preference.key, 60)
            preference.summary = value.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        findPreference<EditTextPreference>("default_interval")?.onPreferenceChangeListener = this
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.timer_preferences)

        var sharedPreferences = preferenceScreen.sharedPreferences
        var preferenceScreen = preferenceScreen

        var count = preferenceScreen.preferenceCount

        for (i in 0 until count) {
            var preference = preferenceScreen.getPreference(i)
            if (preference is ListPreference) {
                var value = sharedPreferences.getString(preference.key, "")!!
                setPreferenceLabel(preference, value)
            } else if (preference is EditTextPreference) {
                var value = sharedPreferences.getInterval(preference.key, 60)
                preference.summary = value.toString()
            }

        }
    }

    private fun setPreferenceLabel(preferences: ListPreference, value: String) {
        var index = preferences.findIndexOfValue(value)
        if (index >= 0) preferences.summary = preferences.entries[index]
    }

}