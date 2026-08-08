package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.ChatMessage
import com.example.data.GeminiRepository
import com.example.data.TerminalNote
import com.example.data.TermuxGuideData
import com.example.data.TermuxGuideItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatDao()
    private val noteDao = db.noteDao()

    private val _customApiKey = MutableStateFlow(prefs.getString("custom_gemini_api_key", "") ?: "")
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    private val _modelTemperature = MutableStateFlow(prefs.getFloat("model_temperature", 0.7f))
    val modelTemperature: StateFlow<Float> = _modelTemperature.asStateFlow()

    val isSystemKeyAvailable: Boolean
        get() = try {
            val key = BuildConfig.GEMINI_API_KEY
            key.isNotBlank() && key != "MY_GEMINI_API_KEY"
        } catch (e: Exception) {
            false
        }

    val keyStatus: StateFlow<String> = _customApiKey.map { key ->
        when {
            key.isNotBlank() -> "Custom Key Active 🟢"
            isSystemKeyAvailable -> "AI Studio Secrets Key Active 🔵"
            else -> "No API Key Configured 🔴"
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Checking...")

    val chatMessages: StateFlow<List<ChatMessage>> = chatDao.getAllMessages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedNotes: StateFlow<List<TerminalNote>> = noteDao.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun saveApiKey(key: String) {
        val trimmed = key.trim()
        _customApiKey.value = trimmed
        prefs.edit().putString("custom_gemini_api_key", trimmed).apply()
    }

    fun clearApiKey() {
        _customApiKey.value = ""
        prefs.edit().remove("custom_gemini_api_key").apply()
    }

    fun updateTemperature(temp: Float) {
        _modelTemperature.value = temp
        prefs.edit().putFloat("model_temperature", temp).apply()
    }

    fun testApiKeyConnection(keyToTest: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val (success, message) = GeminiRepository.testApiKey(keyToTest)
            onResult(success, message)
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank() || _isGenerating.value) return

        viewModelScope.launch {
            val userMsg = ChatMessage(text = userText, isUser = true)
            chatDao.insertMessage(userMsg)
            _isGenerating.value = true

            val currentHistory = chatMessages.value
            val responseText = GeminiRepository.sendMessage(
                history = currentHistory,
                newMessageText = userText,
                customApiKey = _customApiKey.value,
                temperature = _modelTemperature.value
            )

            val botMsg = ChatMessage(text = responseText, isUser = false)
            chatDao.insertMessage(botMsg)
            _isGenerating.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            chatDao.clearHistory()
        }
    }

    fun saveNote(title: String, category: String, command: String, notes: String) {
        viewModelScope.launch {
            val note = TerminalNote(
                title = title,
                category = category,
                command = command,
                notes = notes
            )
            noteDao.insertNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            noteDao.deleteNote(id)
        }
    }

    fun toggleFavoriteNote(note: TerminalNote) {
        viewModelScope.launch {
            noteDao.setFavorite(note.id, !note.isFavorite)
        }
    }

    fun getFilteredGuides(): List<TermuxGuideItem> {
        val q = _searchQuery.value.trim().lowercase()
        if (q.isEmpty()) return TermuxGuideData.guides

        return TermuxGuideData.guides.filter {
            it.title.lowercase().contains(q) ||
            it.description.lowercase().contains(q) ||
            it.command.lowercase().contains(q) ||
            it.category.lowercase().contains(q) ||
            it.tags.any { tag -> tag.lowercase().contains(q) }
        }
    }
}
