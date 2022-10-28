package com.ktt.mylocation

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ktt.mylocation.PermissionUtil.ACTION_START_FUSED_SERVICE
import com.ktt.mylocation.PermissionUtil.ACTION_STOP_FUSED_SERVICE
import com.ktt.mylocation.PermissionUtil.displayLocationSettingsRequest
import com.ktt.mylocation.PermissionUtil.getLocationStatus
import com.ktt.mylocation.databinding.ActivityMainBinding
import com.ktt.mylocation.user.MyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: Toolbar
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var hasLocationPermission = false
    private var lat = ""
    private var lan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolBar)

        setSupportActionBar(toolbar)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

                hasLocationPermission =
                    permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: hasLocationPermission

                if (!hasLocationPermission) {
                    finish()

                } else {
                    showLocationServicePermission()
                    startLocationService()
                }

            }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
            )
        )

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home2,
                R.id.logout

            ), binding.drawer

        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }

    private fun startLocationService() {

        Intent(this, FusedLocationService::class.java).also {
            it.action = ACTION_START_FUSED_SERVICE
            startService(it)
        }

    }

    private fun stopLocationServices() {

        Intent(this, FusedLocationService::class.java).also {
            it.action = ACTION_STOP_FUSED_SERVICE
            startService(it)
        }

    }

    private fun showLocationServicePermission() {
        if (!getLocationStatus(this)) {
            displayLocationSettingsRequest(this, this)
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationService()
    }

    override fun onPause() {
        super.onPause()
        stopLocationServices()
    }

}