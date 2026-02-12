package com.example.project_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.project_1.data.AppViewModel
import com.example.project_1.data.AuthManager

class SavedNumbersFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userId = AuthManager.getCurrentUserId(requireContext())

        return ComposeView(requireContext()).apply {
            setContent {
                SavedNumbersScreen(
                    viewModel = viewModel,
                    userId = userId,
                    onBack = { findNavController().navigateUp() }
                )
            }
        }
    }
}
