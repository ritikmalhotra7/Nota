package com.example.nota.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.apply {
            addNote.apply {
                setOnClickListener {
                   navController
                        .navigate(R.id.action_mainFragment_to_addNoteFragment)
                }
            }
            navController.addOnDestinationChangedListener(object:NavController.OnDestinationChangedListener{
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    Log.d(TAG,controller.currentDestination!!.id.toString()+"    "+R.id.mainFragment.toString())
                    if(controller.currentDestination!!.id == R.id.mainFragment){
                        showToolbar()
                    }else{

                    }
                }
            })
        }
    }

    fun showToolbar() {
        binding.toolbar.setVisibilityForMotionLayout(View.VISIBLE)
    }

    fun hideToolbar() {
        binding.toolbar.setVisibilityForMotionLayout(View.VISIBLE)
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
fun View.setVisibilityForMotionLayout(visibility: Int) {
    val motionLayout = parent as MotionLayout
    motionLayout.constraintSetIds.forEach {
        val constraintSet = motionLayout.getConstraintSet(it) ?: return@forEach
        constraintSet.setVisibility(this.id, visibility)
        constraintSet.applyTo(motionLayout)
    }
}