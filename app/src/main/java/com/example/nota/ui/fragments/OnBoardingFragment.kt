package com.example.nota.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.nota.R
import com.example.nota.databinding.FragmentOnBoardingBinding
import com.example.nota.ui.adapters.ViewPagerAdapter

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding: FragmentOnBoardingBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter()
        binding.viewPager.addOnPageChangeListener(onScrollListener)
    }

    private var onScrollListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when (position) {
                    2 -> {
                        binding.tvNext.apply {
                            text = resources.getString(R.string.finish)
                            setOnClickListener {
                                findNavController().navigate(R.id.action_onBoardingFragment_to_signupFragment)
                            }
                        }
                    }
                    else -> {
                        binding.tvNext.apply {
                            text = resources.getString(R.string.next)
                            setOnClickListener {
                                binding.viewPager.currentItem = position + 1
                            }
                        }
                    }
                }
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        }
}