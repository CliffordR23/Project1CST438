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
            binding.textviewHome.text = "Signed in with Google: ${firebaseUser.email}"
            // Optionally send firebaseUser.uid to remote DB
        } else if (localUserId != -1) {
            binding.textviewHome.text = "Signed in locally: id=$localUserId"
            // Optionally send localUserId to your remote DB
        }

        binding.buttonSubmit.setOnClickListener {
            performPhoneLookup()
        }
        binding.logoutBttn.setOnClickListener {
            AuthManager.logout(requireContext())
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(
                R.id.FirstFragment, null,
                androidx.navigation.NavOptions.Builder().setPopUpTo(R.id.FirstFragment, true).build()
            )
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
            binding.textviewHome.text = "Lookup complete!"
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