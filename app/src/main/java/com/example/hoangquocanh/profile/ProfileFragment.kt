package com.example.hoangquocanh.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.Data
import com.example.hoangquocanh.data.local.user.clearUserInfo
import com.example.hoangquocanh.data.local.user.saveUserInfo
import com.example.hoangquocanh.databinding.FragmentProfileBinding
import com.example.hoangquocanh.login.LoginActivity
import com.example.hoangquocanh.login.UserViewModel
import com.example.hoangquocanh.data.local.user.UserViewModelFactory
import java.io.File

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var imagePath = ""
    private var isChangingPassword = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val factory = UserViewModelFactory(requireActivity().application)
        userViewModel = ViewModelProvider(requireActivity(), factory)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val imageUri: Uri? = result.data?.data
                    if (imageUri != null) {
                        imagePath = getPathFromUri(imageUri)!!
                        Data.currentUser!!.avatar = imagePath
                        loadProfileImage(imagePath)
                    }
                }
            }
        Log.d("AvtProfile",Data.currentUser!!.avatar)
        loadProfileImage(Data.currentUser!!.avatar)
        binding.nameBox.hint = Data.currentUser!!.username
        binding.emailBox.hint = Data.currentUser!!.email
        binding.logoutButton.setOnClickListener {
            onLogout()
        }
        binding.changeAvatarButton.setOnClickListener {
            openImageChooser()
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.profile_to_home)
        }
        binding.confirmButton.setOnClickListener {
            saveInfo()
            findNavController().navigate(R.id.profile_to_home)
        }
        binding.changePasswordButton.setOnClickListener {
            if(isChangingPassword){
                isChangingPassword = false
                binding.oldPasswordBox.visibility = View.GONE
                binding.newPasswordBox.visibility = View.GONE
                binding.confirmPasswordBox.visibility = View.GONE
                binding.nameBox.visibility = View.VISIBLE
                binding.emailBox.visibility = View.VISIBLE
            }else{
                isChangingPassword = true
                binding.oldPasswordBox.visibility = View.VISIBLE
                binding.newPasswordBox.visibility = View.VISIBLE
                binding.confirmPasswordBox.visibility = View.VISIBLE
                binding.nameBox.visibility = View.GONE
                binding.emailBox.visibility = View.GONE
            }
        }
    }

    private fun saveInfo() {
        if (!binding.nameBox.text.isNullOrEmpty())
            Data.currentUser!!.username = binding.nameBox.text.toString()
        if (!binding.emailBox.text.isNullOrEmpty())
            Data.currentUser!!.email = binding.emailBox.text.toString()
        if (binding.oldPasswordBox.text.toString() == Data.currentUser!!.password) {
            if (!binding.newPasswordBox.text.isNullOrEmpty() &&
                binding.newPasswordBox.text.toString() == binding.confirmPasswordBox.text.toString()
            ) {
                Data.currentUser!!.password = binding.newPasswordBox.text.toString()
            }
        }
        if(imagePath.isNotEmpty()){
            Data.currentUser!!.avatar = imagePath
        }
        userViewModel.updateUser(Data.currentUser!!)
        saveUserInfo(requireContext(), Data.currentUser!!)
    }

    private fun onLogout() {
        clearUserInfo(requireContext())
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun getPathFromUri(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return path
    }

    private fun loadProfileImage(path: String) {
        Glide.with(this)
            .load(File(path))
            .error(R.drawable.ic_default_profile)
            .into(binding.profilePic)
    }
}
