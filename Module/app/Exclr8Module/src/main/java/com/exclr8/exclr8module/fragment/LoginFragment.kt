package com.exclr8.exclr8module.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.exclr8.exclr8module.R
import com.exclr8.exclr8module.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        (activity as AppCompatActivity).supportActionBar?.apply { hide() }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                clSplash.isVisible = false
                llLogin.isVisible = true
            }
        }, 1000)

        if (binding.btnLogin.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.ivN1Logo.isVisible = false
            val param = binding.llLogin.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0, 0, 0, 0)
            binding.llLogin.layoutParams = param
            binding.tvLogo.textSize = 24f
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                showToast("Username or Password is Empty")
            } else {
                login()
            }
        }
    }

    private fun login() {
        showToast("Signed In")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}