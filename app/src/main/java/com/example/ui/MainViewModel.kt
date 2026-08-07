package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatDao()
    private val noteDao = db.noteDao()

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

    fun sendMessage(userText: String) {
        if (userText.isBlank() || _isGenerating.value) return

        viewModelScope.launch {
            val userMsg = ChatMessage(text = userText, isUser = true)
            chatDao.insertMessage(userMsg)
            _isGenerating.value = true

            val currentHistory = chatMessages.value
            val responseText = GeminiRepository.sendMessage(currentHistory, userText)

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
