package com.example.project_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project_1.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBttn.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val pass = binding.editTextTextPassword2.text.toString()
            val confirm = binding.editTextTextPassword4.text.toString()

            if (email.isBlank() || pass.isBlank() || confirm.isBlank()) {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass != confirm) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = LocalAuth.signUp(requireContext(), email, pass)
            if (userId == null) {
                Toast.makeText(requireContext(), "An account with this email already exists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            findNavController().navigate(R.id.HomeFragment)
        }

        binding.signUpBttn.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
