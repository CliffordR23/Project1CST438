package com.example.project_1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_1.data.AppViewModel
import com.example.project_1.data.Saved

@Composable
fun SavedNumbersScreen(
    viewModel: AppViewModel,
    userId: Int,
    onBack: () -> Unit
) {
    val savedNumbers by viewModel.selectUserSaved(userId)
        .observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Back button
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Saved Phone Numbers",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (savedNumbers.isEmpty()) {
            Text("No Saved Phone Numbers")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedNumbers) { saved: Saved ->
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = saved.phoneNumber.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )

                            TextButton(
                                onClick = { viewModel.deleteSaved(saved) }
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
