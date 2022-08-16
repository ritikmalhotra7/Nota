package com.example.nota.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.nota.R
import com.example.nota.databinding.ActivityMainBinding
import com.example.nota.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            addNote.setOnClickListener {
                navHostFragment.findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment)
            }
        }
    }
    fun makeLoaderVisible(){
        binding.loader.visibility = View.VISIBLE
    }
    fun makeLoaderGone(){
        binding.loader.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}