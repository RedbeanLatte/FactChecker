package com.redbeanlatte11.factchecker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redbeanlatte11.factchecker.domain.IsSignInUseCase
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import kotlinx.android.synthetic.main.main_act.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_act)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupStartDestination()

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    private fun setupStartDestination() {
        val navHostFragment = nav_host_fragment as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = if (PreferenceUtils.loadSignInResult(applicationContext)) {
            R.id.home_dest
        } else {
            R.id.setup_dest
        }
        navController.graph = navGraph
    }

    private fun setupBottomNavigationBar() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_dest, R.id.channel_dest, R.id.popular_dest, R.id.more_dest
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}