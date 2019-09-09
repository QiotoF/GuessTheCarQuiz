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
import com.pericle.guessthecar.database.Level
import com.pericle.guessthecar.database.LevelDao
import com.pericle.guessthecar.database.LevelDatabase
import com.pericle.guessthecar.databinding.ActivityMainBinding
import com.pericle.guessthecar.entity.Car
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
        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
        NavigationUI.setupWithNavController(binding.navView, navController)
//        binding.navView.setNavigationItemSelectedListener(this)
        val cars = listOf<Car>(
//            Car(listOf("dodge_viper_1.jpg"), "Dodge", "Viper", "USA"),
//            Car(listOf("mini_cooper_1.jpg"), "Mini", "Cooper", "USA"),
//            Car(listOf("nissan_gtr_1.jpg"), "Nissan", "GTR", "Japan"),
//            Car(listOf("saleen_s7_1.jpg"), "Saleen", "S7", "USA"),
//            Car(listOf("toyota_supra_1.jpg", "toyota_supra_2.jpg", "toyota_supra_3.jpg"), "Toyota", "Supra", "Japan")
        )
        val application = requireNotNull(this).application
        levelDao = LevelDatabase.getInstance(application).levelDao
//        carDao = LevelDatabase.getInstance(application).carDao
        uiScope.launch {
            insertAll()
        }

    }

    private suspend fun insertAll() {
        withContext(Dispatchers.IO) {

            levelDao.insert(
                Level(
                    "Brands", QuestionType.BRAND,
                    com.pericle.guessthecar.R.drawable.logo
                )
            )
            levelDao.insert(
                Level(
                    "Models", QuestionType.MODEL,
                    com.pericle.guessthecar.R.drawable.car1
                )
            )
            levelDao.insert(
                Level(
                    "Countries", QuestionType.COUNTRY,
                    com.pericle.guessthecar.R.drawable.countries
                )
            )

//            for (car in cars) {
//                carDao.insert(car)
//            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(com.pericle.guessthecar.R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

}
