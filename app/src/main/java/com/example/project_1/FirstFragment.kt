@file:Suppress("DEPRECATION")

package com.example.project_1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project_1.data.AuthManager
import com.example.project_1.databinding.FragmentFirstBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    // Firebase and Google clients
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var isStartingGoogleSignIn = false
    // Prevent navigating multiple times
    private var navigated = false

    private fun goHomeOnce() {
        if (navigated) return

        val navController = findNavController()
        val currentId = navController.currentDestination?.id
        if (currentId != R.id.FirstFragment && currentId != R.id.SecondFragment) return

        navigated = true
        navController.navigate(R.id.action_FirstFragment_to_HomeFragment)
    }

    // Google sign in result handler
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isStartingGoogleSignIn = false

        if (result.resultCode != android.app.Activity.RESULT_OK) return@registerForActivityResult
        val data = result.data ?: return@registerForActivityResult

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            if (idToken.isNullOrBlank()) {
                Toast.makeText(requireContext(), "No ID token", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            if (!isAdded) return@registerForActivityResult
            firebaseAuthWithGoogle(idToken)

        } catch (e: ApiException) {
            Log.e("AUTH", "Google sign-in failed: ${e.statusCode}", e)
            context?.let { Toast.makeText(it, "Google sign-in failed", Toast.LENGTH_SHORT).show() }
        } catch (t: Throwable) {
            Log.e("AUTH", "CRASH in Google sign-in callback", t)
            context?.let { Toast.makeText(it, "Sign-in crashed: ${t.javaClass.simpleName}", Toast.LENGTH_LONG).show() }
        }
    }

    override fun onCreateView(
        // builds the fragment UI
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // resets nav
        navigated = false

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // if logged in leave asap
        if (alreadyLoggedIn()) {
            binding.btnGoogleSignIn.isEnabled = false
            binding.loginBttn.isEnabled = false
            binding.signUpBttn.isEnabled = false

            view.post { goHomeOnce() }
            return
        }

        // Google sign in config
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Email/password login
        binding.loginBttn.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text?.toString()?.trim().orEmpty()
            val pass = binding.editTextTextPassword2.text?.toString()?.trim().orEmpty()

            if (email.isBlank() || pass.isBlank()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // checks if user exists or else it shows and error
            lifecycleScope.launch {
                val userId = AuthManager.login(requireContext(), email, pass)
                if (userId != null) {
                    goHomeOnce()
                } else {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Google login
        binding.btnGoogleSignIn.setOnClickListener {
            if (alreadyLoggedIn()) {
                // sign out of both local + firebase/google, then start again
                val ctx = context ?: return@setOnClickListener

                AuthManager.logout(ctx)
                FirebaseAuth.getInstance().signOut()

                googleSignInClient.signOut().addOnCompleteListener {
                    startGoogleSignIn()
                }
                return@setOnClickListener
            }

            startGoogleSignIn()
        }

        // Go to sign up screen
        binding.signUpBttn.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    // Google intent
    private fun startGoogleSignIn() {
        if (!::googleSignInClient.isInitialized) return

        isStartingGoogleSignIn = true
        try {
            val intent = googleSignInClient.signInIntent
            signInLauncher.launch(intent)
        } catch (t: Throwable) {
            isStartingGoogleSignIn = false
            Log.e("AUTH", "Failed to launch Google sign-in", t)
            context?.let {
                Toast.makeText(it, "Could not start Google sign-in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Takes google id token and converts it into firebase credential
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val act = activity ?: return

        auth.signInWithCredential(credential)
            .addOnCompleteListener(act) { task ->
                if (!isAdded) return@addOnCompleteListener

                if (task.isSuccessful) {
                    val email = auth.currentUser?.email
                    if (email.isNullOrBlank()) {
                        context?.let {
                            Toast.makeText(it, "Google account has no email", Toast.LENGTH_SHORT).show()
                        }
                        return@addOnCompleteListener
                    }

                    lifecycleScope.launch {
                        val ctx = context ?: return@launch
                        AuthManager.loginOrCreateFromGoogle(ctx, email)
                        goHomeOnce()
                    }
                } else {
                    context?.let {
                        Toast.makeText(it, "Firebase authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


    override fun onStart() {
        super.onStart()

        if (isStartingGoogleSignIn) return

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val ctx = context ?: return
        val localUserId = AuthManager.getCurrentUserId(ctx)

        if (firebaseUser != null || localUserId != -1) {
            view?.post { goHomeOnce() }
        }
    }

    private fun alreadyLoggedIn(): Boolean {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val ctx = context ?: return firebaseUser != null
        val localUserId = AuthManager.getCurrentUserId(ctx)
        return firebaseUser != null || localUserId != -1
    }


    // clears view/fragments
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
