package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatSessionEntity
import com.example.data.local.LumaDatabase
import com.example.data.model.AppLanguage
import com.example.data.repository.LumaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LumaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LumaRepository

    val allSessions: StateFlow<List<ChatSessionEntity>>

    private val _currentSessionId = MutableStateFlow<String?>(null)
    val currentSessionId: StateFlow<String?> = _currentSessionId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMessages: StateFlow<List<ChatMessageEntity>>

    private val _selectedLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedModel = MutableStateFlow("gemini-3.5-flash")
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    private val _temperature = MutableStateFlow(0.7f)
    val temperature: StateFlow<Float> = _temperature.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    init {
        val database = LumaDatabase.getDatabase(application)
        repository = LumaRepository(database.chatDao())

        allSessions = repository.allSessions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        currentMessages = _currentSessionId.flatMapLatest { sessionId ->
            if (sessionId != null) {
                repository.getMessagesForSession(sessionId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Initialize first session automatically if none exists
        viewModelScope.launch {
            allSessions.collect { sessions ->
                if (sessions.isEmpty() && _currentSessionId.value == null) {
                    createNewChat()
                } else if (_currentSessionId.value == null && sessions.isNotEmpty()) {
                    _currentSessionId.value = sessions.first().id
                }
            }
        }
    }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    fun setModel(model: String) {
        _selectedModel.value = model
    }

    fun setTemperature(temp: Float) {
        _temperature.value = temp
    }

    fun setShowSettingsDialog(show: Boolean) {
        _showSettingsDialog.value = show
    }

    fun createNewChat() {
        viewModelScope.launch {
            val title = when (_selectedLanguage.value) {
                AppLanguage.KURDISH_SORANI -> "گفتوگۆی نوێ"
                AppLanguage.KURDISH_BADINI -> "ئاخفتنا نوو"
                AppLanguage.ARABIC -> "محادثة جديدة"
                else -> "New Chat"
            }
            val newId = repository.createNewSession(title, _selectedLanguage.value.code)
            _currentSessionId.value = newId
        }
    }

    fun selectSession(sessionId: String) {
        _currentSessionId.value = sessionId
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            if (_currentSessionId.value == sessionId) {
                val remaining = allSessions.value.filter { it.id != sessionId }
                if (remaining.isNotEmpty()) {
                    _currentSessionId.value = remaining.first().id
                } else {
                    createNewChat()
                }
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllHistory()
            _currentSessionId.value = null
            createNewChat()
        }
    }

    fun sendMessage(textOverride: String? = null) {
        val textToSend = textOverride ?: _inputText.value.trim()
        if (textToSend.isBlank() || _isGenerating.value) return

        val sessionId = _currentSessionId.value ?: return

        _inputText.value = ""
        _isGenerating.value = true

        viewModelScope.launch {
            repository.sendMessage(
                sessionId = sessionId,
                userText = textToSend,
                selectedLanguage = _selectedLanguage.value,
                modelName = _selectedModel.value,
                temperature = _temperature.value,
                onChunk = {
                    // Reactive stream updates DAO
                }
            )
            _isGenerating.value = false
        }
    }
}
