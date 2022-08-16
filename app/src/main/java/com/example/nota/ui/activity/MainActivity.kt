package com.example.nota.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.nota.R
import com.example.nota.databinding.ActivityMainBinding
import com.example.nota.utils.Constants.TAG
import com.example.nota.utils.makeGone
import com.example.nota.utils.makeVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.apply {
            addNote.apply {
                setOnClickListener {
                    navController
                        .navigate(R.id.action_mainFragment_to_addNoteFragment)
                }
            }

            navController.addOnDestinationChangedListener { controller, destination, arguments ->

            }
        }
    }

    fun showToolbar() {
        binding.toolbar.makeVisible()
        binding.addNote.makeVisible()
        binding.textView.makeVisible()
    }

    private fun hideToolbar() {
        binding.toolbar.makeGone()
        binding.addNote.makeGone()
        binding.textView.makeGone()
    }

    fun makeLoaderVisible() {
        binding.loader.makeVisible()
    }

    fun makeLoaderGone() {
        binding.loader.makeGone()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}