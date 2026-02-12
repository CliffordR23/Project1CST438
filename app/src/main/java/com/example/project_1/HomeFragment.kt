@file:Suppress("DEPRECATION")

package com.example.project_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.project_1.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import com.example.project_1.data.AuthManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val ACCESS_KEY = "2d63b1eea74b9e6c3f951c40ab6b3221"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val localUserId = AuthManager.getCurrentUserId(requireContext())

        if (firebaseUser != null) {
            binding.textviewHome.text =
                getString(R.string.signed_in_google, firebaseUser.email ?: "")
        } else if (localUserId != -1) {
            binding.textviewHome.text =
                getString(R.string.signed_in_local, localUserId)
        }


        binding.buttonSubmit.setOnClickListener {
            val phoneNumber = binding.edittextPhoneLookup.text.toString()
            if (phoneNumber.isNotEmpty()) {
                performPhoneLookup(phoneNumber)
            } else {
                Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }
        binding.logoutBttn.setOnClickListener {
            val ctx = context ?: return@setOnClickListener

            // Local logout
            AuthManager.logout(ctx)

            //Firebase logout
            FirebaseAuth.getInstance().signOut()

            // Google logout
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            GoogleSignIn.getClient(ctx, gso).signOut().addOnCompleteListener {

                // 4) Navigate back to FirstFragment and clear Home from back stack
                findNavController().navigate(
                    R.id.FirstFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.HomeFragment, true) // remove Home from backstack
                        .build()
                )
            }
        }
    }

    private fun performPhoneLookup(phoneNumber: String) {
        setLoading(true)

        lifecycleScope.launch {
            try {
                val response = NumVerifyClient.service.verifyPhoneNumber(ACCESS_KEY, phoneNumber)
                setLoading(false)

                if (response.valid) {
                    val intent = Intent(requireContext(), VerificationActivity::class.java).apply {
                        putExtra("number", response.number)
                        putExtra("valid", response.valid)
                        putExtra("country", response.country_name)
                        putExtra("location", response.location)
                        putExtra("carrier", response.carrier)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Unsuccessful: Invalid phone number", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                setLoading(false)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonSubmit.isEnabled = !isLoading
        binding.edittextPhoneLookup.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}