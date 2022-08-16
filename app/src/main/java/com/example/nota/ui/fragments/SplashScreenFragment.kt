package com.example.nota.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nota.R
import com.example.nota.databinding.FragmentSplashScreenBinding
import com.example.nota.utils.Constants.SPLASH_SCREEN_KEY
import com.example.nota.utils.readString
import com.example.nota.utils.saveString
import com.example.nota.utils.toast

class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding: FragmentSplashScreenBinding get() = _binding!!

    private lateinit var varContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        varContext = requireContext()
        splashScreenWorking()
        return binding.root
    }

    private fun splashScreenWorking() {
        if (varContext.readString(SPLASH_SCREEN_KEY) == null) {
            if (varContext.saveString(
                    SPLASH_SCREEN_KEY,
                    getString(R.string.You_have_logged_in_before)
                )
            ) {
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                }, 3000)
            } else {
                varContext.toast(getString(R.string.something_wrong_happened))
            }
        } else {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashScreenFragment_to_signupFragment)
            }, 3000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}