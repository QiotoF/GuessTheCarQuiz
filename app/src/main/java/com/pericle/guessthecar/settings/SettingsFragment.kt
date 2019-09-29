package com.pericle.guessthecar.settings


import android.R
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pericle.guessthecar.database.LevelDatabase
import com.pericle.guessthecar.entity.Car
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
        repository = LevelRepository(LevelDatabase.getInstance(this.context!!).levelDao)
        findPreference("delete").setOnPreferenceClickListener {

            showDeleteDialog()

            true
        }

//        findPreference("add").setOnPreferenceClickListener {
//            addData()
//
//            true
//        }
    }

    private fun addData() {
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val carsRef = storageRef.child("cars")
        carsRef.listAll().addOnCompleteListener {
            val cars = mutableMapOf<String, Car>()
            val result = it.result
            val items = result?.items
            for (item in items!!) {
                val name = item.name.substringBeforeLast(" ")
                val brand = name.substringBefore(" ")
                val model = name.substringAfter(" ")
                val images = mutableListOf<String>()
                val car = Car(
                    brand = brand,
                    model = model,
                    images = images
                )
                if (!cars.containsKey("name")) {
                    cars[name] = car
                }
                item.downloadUrl.addOnCompleteListener { uri ->
                    cars[name]?.images?.add(uri.result.toString())
                    if (item == items.last()) {
                        for (curcar in cars) {
                            db.collection("cars").document(curcar.key).set(curcar.value)
                        }
                    }
                }

            }
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(context!!)
            .setTitle("Warning")
            .setMessage("Are you sure you want to delete all progress?")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                // Continue with delete operation
                deleteProgress()
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(R.string.no, null)
            .setIcon(R.drawable.ic_dialog_alert)
            .show()
    }

    private fun deleteProgress() {
        uiScope.launch {
            repository.deleteAllProgress()
        }
    }
}