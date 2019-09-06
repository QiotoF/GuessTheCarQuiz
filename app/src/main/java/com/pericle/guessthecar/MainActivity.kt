package com.pericle.guessthecar

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.pericle.guessthecar.database.*
import com.pericle.guessthecar.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.rate -> rateApp(this)
            R.id.share -> shareApp(this)
            R.id.privacy_policy -> openPrivacyPolicy(this)
            R.id.exit -> finish()
        }
        return true
    }


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var levelDao: LevelDao
//    private lateinit var carDao: CarDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.nav_host_fragment)
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
        binding.navView.setNavigationItemSelectedListener(this)
        val cars = listOf<Car>(
//            Car(listOf("dodge_viper_1.jpg"), "Dodge", "Viper", "USA"),
//            Car(listOf("mini_cooper_1.jpg"), "Mini", "Cooper", "USA"),
//            Car(listOf("nissan_gtr_1.jpg"), "Nissan", "GTR", "Japan"),
//            Car(listOf("saleen_s7_1.jpg"), "Saleen", "S7", "USA"),
//            Car(listOf("toyota_supra_1.jpg", "toyota_supra_2.jpg", "toyota_supra_3.jpg"), "Toyota", "Supra", "Japan")
        )
        val application = requireNotNull(this).application
        levelDao = MyDatabase.getInstance(application).levelDao
//        carDao = MyDatabase.getInstance(application).carDao
        uiScope.launch {
            insertAll(cars)
        }

    }

    private suspend fun insertAll(cars: List<Car>) {
        withContext(Dispatchers.IO) {

            levelDao.insert(Level("Brands", QuestionType.BRAND, R.drawable.logo))
            levelDao.insert(Level("Models", QuestionType.MODEL, R.drawable.car1))
            levelDao.insert(Level("Countries", QuestionType.COUNTRY, R.drawable.countries))

//            for (car in cars) {
//                carDao.insert(car)
//            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

}
