package com.pericle.guessthecar.settings


import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceFragmentCompat
import com.pericle.guessthecar.database.MyDatabase
import com.pericle.guessthecar.repository.LevelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SettingsFragment : PreferenceFragmentCompat() {


    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var repository: LevelRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(com.pericle.guessthecar.R.xml.preferences, rootKey)
        repository = LevelRepository(MyDatabase.getInstance(this.context!!).levelDao)
        findPreference("delete").setOnPreferenceClickListener {

            AlertDialog.Builder(context!!)
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete all progress?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                    deleteProgress()
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

            true
        }
    }

    private fun deleteProgress() {

        uiScope.launch {
            repository.deleteAllProgress()
        }
    }
}