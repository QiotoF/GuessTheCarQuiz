package com.pericle.guessthecar.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.pericle.guessthecar.R
import com.pericle.guessthecar.database.Level
import com.pericle.guessthecar.database.LevelDao
import com.pericle.guessthecar.database.LevelDatabase
import com.pericle.guessthecar.databinding.ActivityMainBinding
import com.pericle.guessthecar.entity.QuestionType
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var levelDao: LevelDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenLayoutSize =
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        if (screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_SMALL || screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                com.pericle.guessthecar.R.layout.activity_main
            )
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(com.pericle.guessthecar.R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
        NavigationUI.setupWithNavController(binding.navView, navController)
        val application = requireNotNull(this).application
        levelDao = LevelDatabase.getInstance(application).levelDao
        uiScope.launch {
            insertAll()
        }

    }

    private suspend fun insertAll() {
        withContext(Dispatchers.IO) {

            levelDao.insert(
                Level(
                    getString(R.string.brands), QuestionType.BRAND, R.drawable.logo
                )
            )
            levelDao.insert(
                Level(
                    getString(R.string.models), QuestionType.MODEL, R.drawable.car1
                )
            )
            levelDao.insert(
                Level(
                    getString(R.string.countries), QuestionType.COUNTRY, R.drawable.countries
                )
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(com.pericle.guessthecar.R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

}
