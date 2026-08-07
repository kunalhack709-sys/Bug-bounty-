package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GeminiRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null,
    val generationConfig: GenerationConfig? = null
)

data class Content(
    val parts: List<Part>,
    val role: String? = null
)

data class Part(
    val text: String? = null
)

data class GenerationConfig(
    val temperature: Float? = 0.7f,
    val topP: Float? = 0.95f
)

data class GeminiResponse(
    val candidates: List<Candidate>? = null
)

data class Candidate(
    val content: Content? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiRepository {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val apiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }

    private val SYSTEM_INSTRUCTION = Content(
        parts = listOf(
            Part(
                text = """
                You are Bug Bounty AI, an ethical security research methodology advisor and Android Termux environment assistant.
                
                YOUR SCOPE & GUIDELINES:
                1. Focus strictly on ethical security methodologies, OWASP guidelines, vulnerability analysis frameworks, bug reporting formats, and Termux CLI command workflows on Android.
                2. Explain security research techniques clearly and educationally (e.g., recon methodology, port scanning parameters, directory fuzzing setup, HTTP request analysis, Android APK decompilation commands with jadx/apktool).
                3. Do NOT generate functional attack payloads, actionable exploits, or malicious code designed to harm specific targets or bypass security controls.
                4. Always advocate for proper authorization, scope adherence, bug bounty platform rules (HackerOne, Bugcrowd, Intigriti), and responsible disclosure.
                5. Provide helpful Termux setup tips for Android bug bounty hunting (e.g., pkg install git python curl nmap, setting up bash aliases, Go environment setup in Termux, memory management on mobile devices).
                """.trimIndent()
            )
        )
    )

    suspend fun sendMessage(history: List<ChatMessage>, newMessageText: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key missing or unconfigured. Please add your GEMINI_API_KEY in the AI Studio Secrets panel."
        }

        val contentList = mutableListOf<Content>()
        
        // Include up to last 10 turns of history for context
        val recentHistory = history.takeLast(10)
        for (msg in recentHistory) {
            contentList.add(
                Content(
                    role = if (msg.isUser) "user" else "model",
                    parts = listOf(Part(text = msg.text))
                )
            )
        }
        
        contentList.add(
            Content(
                role = "user",
                parts = listOf(Part(text = newMessageText))
            )
        )

        val request = GeminiRequest(
            contents = contentList,
            systemInstruction = SYSTEM_INSTRUCTION,
            generationConfig = GenerationConfig(temperature = 0.7f)
        )

        try {
            val response = apiService.generateContent(apiKey, request)
            val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            reply ?: "No response generated from Gemini API."
        } catch (e: Exception) {
            "Error contacting Gemini API: ${e.localizedMessage ?: e.message ?: "Unknown error"}"
        }
    }
}
