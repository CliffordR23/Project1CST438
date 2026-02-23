package com.example.project_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.example.project_1.data.AuthManager
import com.google.firebase.auth.FirebaseAuth

class LandingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Ensures the Compose UI is disposed when the Fragment view is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MaterialTheme {
                    LandingScreen(
                        onPhoneLookup = {
                            findNavController().navigate(R.id.action_LandingFragment_to_HomeFragment)
                        },
                        onSearchHistory = {
                            findNavController().navigate(R.id.action_LandingFragment_to_HistoryFragment)
                        },
                        onSavedNumbers = {
                            findNavController().navigate(R.id.action_LandingFragment_to_SavedFragment)
                        },
                        onLogout = {
                            val context = requireContext();
                            AuthManager.logout(context);
                            FirebaseAuth.getInstance().signOut()

                            findNavController().navigate(
                                R.id.FirstFragment,
                                null,
                                androidx.navigation.NavOptions.Builder()
                                    .setPopUpTo(R.id.LandingFragment, true)
                                    .build()
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun LandingScreen(
        onPhoneLookup: () -> Unit,
        onSearchHistory: () -> Unit,
        onSavedNumbers: () -> Unit,
        onLogout: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Landing Page",
                fontSize = 32.sp
            )

            Spacer(Modifier.height(48.dp))

            Button(onClick = onPhoneLookup) { Text("Phone Lookup") }
            Spacer(Modifier.height(24.dp))

            Button(onClick = onSearchHistory) { Text("Search History") }
            Spacer(Modifier.height(24.dp))

            Button(onClick = onSavedNumbers) { Text("Saved Numbers") }
            Spacer(Modifier.height(24.dp))

            Button(onClick = onLogout) { Text("Sign Out") }
        }
    }

    @Preview(showBackground = true, name = "Landing Screen Preview")
    @Composable
    fun LandingScreenPreview() {
        MaterialTheme {
            LandingScreen(
                onPhoneLookup = {},
                onSearchHistory = {},
                onSavedNumbers = {},
                onLogout = {}
            )
        }
    }
}