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
import com.example.nota.databinding.FragmentLoginBinding
import com.example.nota.models.UserRequest
import com.example.nota.utils.Resource
import com.example.nota.utils.TokenManager
import com.example.nota.utils.validateCredentials
import com.example.nota.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private lateinit var email: String
    private lateinit var password: String

    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()

    private lateinit var varContext: Context

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
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

            btnLogin.setOnClickListener {
                setUserRequest()
                val credPair = this.root.validateCredentials(email = email, password = password, isLogin = true)
                if (credPair.first) {
                    authViewModel.loginUser(UserRequest(email, password))
                } else {
                    txtError.text = credPair.second
                }
            }
            btnSignUp.setOnClickListener{
                findNavController().popBackStack()
            }
        }
    }

    private fun FragmentLoginBinding.setUserRequest() {
        email = txtEmail.text.toString()
        password = txtPassword.text.toString()
    }

    private fun observeUserData() {
        authViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.txtError.text = it.message.toString()
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}