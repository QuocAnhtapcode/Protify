package com.example.hoangquocanh.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hoangquocanh.MainActivity
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.local.user.User
import com.example.hoangquocanh.data.local.user.UserViewModelFactory
import com.example.hoangquocanh.data.local.user.saveUserInfo
import com.example.hoangquocanh.databinding.FragmentLoginBinding


class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val factory = UserViewModelFactory(requireActivity().application)
        userViewModel = ViewModelProvider(requireActivity(),factory)[UserViewModel::class.java]
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener{
            return@setOnClickListener
        }
        binding.register.setOnClickListener{
            findNavController().navigate(R.id.action_login_to_register)
        }
        binding.loginButton.setOnClickListener{
            val username = binding.usernameText.text.toString()
            val password = binding.passwordText.text.toString()
            userViewModel.getUser(username, password).observe(requireActivity()) { user ->
                if (user != null) {
                    onLoginSuccess(user)
                } else {
                    Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun onLoginSuccess(user: User) {
        saveUserInfo(requireContext(),user)
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}