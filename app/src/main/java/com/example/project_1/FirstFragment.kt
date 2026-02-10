package com.example.project_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project_1.databinding.FragmentFirstBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    // Firebase and Google clients
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the returned intent from Google Sign-In
        val data: Intent? = result.data
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(Exception::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                firebaseAuthWithGoogle(idToken)
            } else {
                Log.e("AUTH", "Google returned null idToken")
            }
        } catch (e: Exception) {
            Log.e("AUTH", "Google sign-in failed", e)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // firebase
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(requireContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.loginBttn.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword2.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                android.widget.Toast.makeText(requireContext(), "Please enter email and password", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = LocalAuth.login(requireContext(), email, password)
            if (success) {
                findNavController().navigate(R.id.HomeFragment)
            } else {
                android.widget.Toast.makeText(requireContext(), "Invalid email or password", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        //google login
        binding.btnGoogleSignIn.setOnClickListener {
            startGoogleSignIn()
        }

        //go to sign up screen
        binding.signUpBttn.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }



    private fun startGoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("AUTH", "Firebase sign-in OK: ${user?.email} uid=${user?.uid}")
                    findNavController().navigate(R.id.HomeFragment)                } else {
                    Log.e("AUTH", "Firebase sign-in FAILED", task.exception)
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val localLoggedIn = LocalAuth.isLoggedIn(requireContext())

        if (firebaseUser != null || localLoggedIn) {
            findNavController().navigate(R.id.HomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
