package com.example.project_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.project_1.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Firebase + Google Sign-in clients
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    // Activity Result launcher for Google Sign-In
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(Exception::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                } else {
                    Toast.makeText(this, "No ID token returned from Google", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setSupportActionBar(binding.toolbar)
        // val navController = findNavController(R.id.nav_host_fragment_content_main)
        // appBarConfiguration = AppBarConfiguration(navController.graph)
        // setupActionBarWithNavController(navController, appBarConfiguration)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In to request the ID token (default_web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Wire sign-in button if it exists in your layout.
        // Add a Button with id btnGoogleSignIn to activity_main.xml OR use your FAB by changing this reference.
        binding.root.findViewById<android.view.View?>(resources.getIdentifier("btnGoogleSignIn", "id", packageName))
            ?.setOnClickListener {
                signInWithGoogle()
            }

        // Example: if you prefer to use the FAB you had commented out earlier:
        // binding.fab.setOnClickListener { signInWithGoogle() }

        // Monitor auth state (optional) - update UI when user signs in/out
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // user signed in
                Toast.makeText(this, "Signed in as ${user.email}", Toast.LENGTH_SHORT).show()
                // e.g. navigate to a logged-in fragment or update UI
            } else {
                // user signed out
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in: ${user?.email}", Toast.LENGTH_SHORT).show()
                    // TODO: navigate to main screen or update UI
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Optional sign-out helper you can call from a menu or button
    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
            .addOnCompleteListener {
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
            }
    }

    // keep your existing menu/nav code
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
