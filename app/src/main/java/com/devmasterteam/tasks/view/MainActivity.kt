package com.devmasterteam.tasks.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.NavigationUI
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityMainBinding
import com.devmasterteam.tasks.utils.Navigator
import com.devmasterteam.tasks.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private var appBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMainBinding? = null
    private var viewModel: MainViewModel? = null
    private var navigator: Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initialize()
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding?.apply {
            appBarMain.fab.setOnClickListener {
                navigator?.goToTaskForm()
            }
            setSupportActionBar(appBarMain.toolbar)
        }
        // Navegação
        setupNavigation()
        viewModel?.loadUserName()
        // Observadores
        observe()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {
        binding?.apply {
            val drawerLayout: DrawerLayout = drawerLayout
            val navView: NavigationView = navView
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            appBarConfiguration = AppBarConfiguration(
                setOf(R.id.nav_all_tasks, R.id.nav_next_tasks, R.id.nav_expired), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration!!)
            navView.setupWithNavController(navController)

            navView.setNavigationItemSelectedListener {
                if (it.itemId == R.id.nav_logout) {
                    viewModel?.logout()
                    navigator?.goToLogin()
                    finish()
                } else {
                    NavigationUI.onNavDestinationSelected(it, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                true
            }
        }
    }

    private fun observe() {
        viewModel?.name?.observe(this) {
            val header = binding?.navView?.getHeaderView(0)
            header?.findViewById<TextView>(R.id.text_name)?.text = it
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}