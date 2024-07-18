package com.example.hoangquocanh.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hoangquocanh.MainActivity
import com.example.hoangquocanh.data.local.user.User
import com.example.hoangquocanh.data.local.user.UserViewModelFactory
import com.example.hoangquocanh.data.local.user.saveUserInfo
import com.example.hoangquocanh.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get()= _binding!!
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        val factory = UserViewModelFactory(requireActivity().application)
        userViewModel = ViewModelProvider(requireActivity(),factory)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernamePattern = Regex("^[a-zA-Z0-9]+$")
        // Chỉ chấp nhận chữ cái và số
        val emailPattern = Regex("^\\S+@apero\\.vn\$")
        // Đuôi email phải là @apero.vn và không có dấu cách
        val phoneNumberPattern = Regex("^\\d{10,11}\$")
        // Chỉ chấp nhận số, có độ dài từ 10 đến 11 ký tự
        val passwordPattern = Regex("^[a-zA-Z0-9]+$")
        // Chỉ chấp nhận chữ cái và số

        binding.registerButton.setOnClickListener{
            val username = binding.usernameText.text.toString()
            if (!username.matches(usernamePattern)) {
                Toast.makeText(requireContext(), "Invalid username!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val email = binding.emailText.text.toString()
            if (!email.matches(emailPattern)) {
                Toast.makeText(requireContext(), "Invalid email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val password = binding.passwordText.text.toString()
            if (!password.matches(passwordPattern)) {
                Toast.makeText(requireContext(), "Invalid password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val confirmPassword = binding.confirmPasswordText.text.toString()
            if(confirmPassword != password){
                Toast.makeText(requireContext(), "Unmatched password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("RegisterHehe",username)
            val user = User(avatar = "", username=username,
                email=email, phoneNumber = "",password=password)
            userViewModel.addUser(user)
            onLoginSuccess(user)
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