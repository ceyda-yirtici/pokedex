package com.example.movieproject

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.example.movieproject.databinding.ActivityMainBinding
import com.example.movieproject.ui.cast.CastFragment
import com.example.movieproject.ui.discovered.DiscoveredFragment
import com.example.movieproject.ui.moviedetail.DetailMovieFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide() // Hide the default action bar

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

       val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_movies, R.id.navigation_favorites, R.id.navigation_discover,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.END).excludeTarget(R.id.nav_host_fragment_activity_main, true))
                when (f) {
                    is DetailMovieFragment -> {
                        binding.navView.visibility = View.GONE
                    }
                    is CastFragment  -> {
                    binding.navView.visibility = View.GONE
                     }
                    is DiscoveredFragment -> {
                    binding.navView.visibility = View.GONE
                    }
                    else -> {
                        binding.navView.visibility = View.VISIBLE
                    }
                }
            }
        }, true)

    }

    private fun setBottomNavigationVisibility(visibility: Int) {
        binding.navView.visibility = visibility
    }


}