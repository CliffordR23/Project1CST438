package com.example.project_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.project_1.data.AppViewModel
import com.example.project_1.data.AuthManager
import com.example.project_1.data.History

class HistoryFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val userId = AuthManager.getCurrentUserId(requireContext())

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {

                    if (userId == -1) {
                        // Not logged in → leave
                        findNavController().navigateUp()
                    } else {
                        val history by viewModel
                            .selectUserHistory(userId)
                            .observeAsState(emptyList())

                        HistoryScreen(
                            userId = userId,
                            history = history,
                            onDelete = { viewModel.deleteHistory(it) },
                            onBack = { findNavController().navigate(R.id.LandingFragment) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryScreen(
    userId: Int,
    history: List<History>,
    onDelete: (History) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))


        Text(
            text = "Search History",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Logged in user ID: $userId",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        if (history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No History")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(history, key = { it.historyID }) { h ->
                    HistoryRow(h, onDelete)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
private fun HistoryRow(
    item: History,
    onDelete: (History) -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(item.phoneNumber.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(item.time)
            Spacer(Modifier.height(6.dp))
            Text(item.data)

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = { onDelete(item) }) {
                Text("Delete")
            }
        }
    }
}