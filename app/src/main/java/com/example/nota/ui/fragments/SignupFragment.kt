package com.example.nota.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nota.R
import com.example.nota.databinding.FragmentSignupBinding
import com.example.nota.models.UserRequest
import com.example.nota.ui.activity.MainActivity
import com.example.nota.utils.Resource
import com.example.nota.utils.TokenManager
import com.example.nota.utils.validateCredentials
import com.example.nota.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding get() = _binding!!

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String

    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()

    private lateinit var varContext: Context

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(layoutInflater)
        if (tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_signupFragment_to_mainFragment)
        }
        varContext = requireContext()
        setUpUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserData()
    }

    private fun setUpUI() {
        binding.apply {

            btnSignUp.setOnClickListener {
                setUserRequest()
                val credPair = this.root.validateCredentials(userName, email, password, false)
                if (credPair.first) {
                    authViewModel.registerUser(UserRequest(email, password, userName))
                } else {
                    txtError.text = credPair.second
                }
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }
    }

    private fun FragmentSignupBinding.setUserRequest() {
        email = txtEmail.text.toString()
        password = txtPassword.text.toString()
        userName = txtUsername.text.toString()
    }

    private fun observeUserData() {
        authViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_signupFragment_to_mainFragment)
                    (activity as MainActivity).makeLoaderGone()
                }
                is Resource.Error -> {
                    binding.txtError.text = it.message.toString()
                    (activity as MainActivity).makeLoaderGone()
                }
                is Resource.Loading -> {
                    (activity as MainActivity).makeLoaderVisible()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}