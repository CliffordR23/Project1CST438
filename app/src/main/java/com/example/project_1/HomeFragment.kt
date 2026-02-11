@file:Suppress("DEPRECATION")

package com.example.project_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project_1.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import com.example.project_1.data.AuthManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
            performPhoneLookup()
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

    private fun performPhoneLookup() {
        // Show loading state
        setLoading(true)

        // Mocking a long response (Replace with your actual API call)
        binding.root.postDelayed({
            // Hide loading state after response
            setLoading(false)
            
            // Handle result here
            binding.textviewHome.text = getString(R.string.lookup_complete)
        }, 2000) // 2 second delay
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        
        // Optionally disable input during loading
        binding.buttonSubmit.isEnabled = !isLoading
        binding.edittextPhoneLookup.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}