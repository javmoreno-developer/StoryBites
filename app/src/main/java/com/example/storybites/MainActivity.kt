package com.example.storybites

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.storybites.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)




        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        // Tenemos que configurar de forma apropiada en boton atras indicando
        // al controlador cuales son las opciones del menu por las que podemos navegar.
        var abc = AppBarConfiguration(setOf(R.id.mainFragment, R.id.settingsFragment))

        with(binding) {
            navBotMenu.setupWithNavController(navController)
            setupActionBarWithNavController(navController, abc)

        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.coreFragment || destination.id == R.id.readFragment || destination.id == R.id.detailBookFragment) {

                binding.navBotMenu.visibility = View.GONE
            } else {

                binding.navBotMenu.visibility = View.VISIBLE
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        //openNavBar()
    }

    /* fun hideNavBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
    }
    fun openNavBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

*/


}