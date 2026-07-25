package com.example.ui.screens

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.DrawerContent
import com.example.ui.components.SettingsDialog
import com.example.ui.viewmodel.LumaViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: LumaViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val sessions by viewModel.allSessions.collectAsState()
    val currentSessionId by viewModel.currentSessionId.collectAsState()
    val currentMessages by viewModel.currentMessages.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val temperature by viewModel.temperature.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val showSettingsDialog by viewModel.showSettingsDialog.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                sessions = sessions,
                currentSessionId = currentSessionId,
                selectedLanguage = selectedLanguage,
                onSessionSelect = { sessionId ->
                    viewModel.selectSession(sessionId)
                    scope.launch { drawerState.close() }
                },
                onNewChatClick = {
                    viewModel.createNewChat()
                    scope.launch { drawerState.close() }
                },
                onDeleteSessionClick = { sessionId ->
                    viewModel.deleteSession(sessionId)
                },
                onLanguageSelect = { language ->
                    viewModel.setLanguage(language)
                },
                onOpenSettings = {
                    scope.launch { drawerState.close() }
                    viewModel.setShowSettingsDialog(true)
                }
            )
        }
    ) {
        ChatScreen(
            messages = currentMessages,
            inputText = inputText,
            isGenerating = isGenerating,
            selectedLanguage = selectedLanguage,
            selectedModel = selectedModel,
            onInputTextChange = { viewModel.updateInputText(it) },
            onSendMessage = { promptOverride -> viewModel.sendMessage(promptOverride) },
            onOpenDrawer = { scope.launch { drawerState.open() } },
            onOpenSettings = { viewModel.setShowSettingsDialog(true) },
            modifier = modifier
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            selectedLanguage = selectedLanguage,
            selectedModel = selectedModel,
            temperature = temperature,
            onLanguageChange = { viewModel.setLanguage(it) },
            onModelChange = { viewModel.setModel(it) },
            onTemperatureChange = { viewModel.setTemperature(it) },
            onClearAllClick = { viewModel.clearAllData() },
            onDismiss = { viewModel.setShowSettingsDialog(false) }
        )
    }
}
