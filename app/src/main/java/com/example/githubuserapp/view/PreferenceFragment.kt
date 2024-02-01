package com.example.githubuserapp.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.githubuserapp.R
import com.example.githubuserapp.alarm.AlarmReceiver

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var spReminder: SwitchPreference
    private lateinit var spLanguage: Preference

    private lateinit var languageChange: String
    private lateinit var reminder:String
    private lateinit var alarmReceiver: AlarmReceiver
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
        spLanguage.setOnPreferenceClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            true
        }
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == reminder){
            if (spReminder.isChecked){
                alarmReceiver.setRepeatingAlarm(
                    requireContext(), AlarmReceiver.TYPE_REPEATING, getString(R.string.reminder_message), getString(R.string.reminder_toast_on)
                )
            }else{
                alarmReceiver.cancelAlarm(requireContext(), getString(R.string.reminder_toast_off))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
    private fun init(){
        reminder = resources.getString(R.string.key_reminder)
        languageChange = resources.getString(R.string.key_change_language)
        spReminder = findPreference<SwitchPreference>(reminder) as SwitchPreference
        spLanguage = findPreference<Preference>(languageChange) as Preference
        alarmReceiver = AlarmReceiver()
    }
    private fun setSummaries(){
        val sh = preferenceManager.sharedPreferences
        spReminder.isChecked = sh.getBoolean(reminder, false)
    }

}