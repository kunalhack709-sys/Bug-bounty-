package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
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
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiRepository {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val CANDIDATE_MODELS = listOf(
        "gemini-2.5-flash",
        "gemini-2.0-flash",
        "gemini-1.5-flash",
        "gemini-flash-latest",
        "gemini-2.5-flash-lite"
    )

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
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
                You are Bug Bounty AI, an elite, authoritative ethical hacking & security research advisor and Android Termux specialist.

                YOUR CORE SCOPE & CAPABILITIES:
                1. Web Application Penetration Testing: Deep knowledge of OWASP Top 10 (SQLi, XSS, SSRF, IDOR/BOLA, CSRF, CORS, LFI/RFI, Business Logic Flaws, JWT, OAuth vulnerabilities).
                2. Reconnaissance & Asset Discovery: Subdomain enumeration (subfinder, assetfinder, amass), live probing (httpx), URL mining (waybackurls, gau), parameter discovery (arjun).
                3. Mobile & Android SAST: Reverse engineering APKs with JADX, searching for hardcoded secrets, analyzing AndroidManifest.xml exported components, Frida/Objection runtime manipulation.
                4. API & GraphQL Security: BOLA/BFLA testing, GraphQL schema introspection, mass assignment, rate limiting bypasses.
                5. Content Discovery & Fuzzing: Path fuzzing with ffuf/dirsearch, wordlist selection, WAF evasion techniques.
                6. Termux CLI Environment: Command execution in Termux, package installations, Golang setup, Python virtual environments, background wakelocks.
                7. Bug Bounty Platform Methodologies: Professional bug report writing for HackerOne, Bugcrowd, Intigriti, CVSS 3.1 scoring, writing clear reproduction steps.

                GUIDELINES:
                - Explain security concepts clearly, educationally, and comprehensively with practical CLI commands, HTTP request/response examples, and mitigation steps.
                - Always advocate for authorization, scope adherence, and responsible disclosure. Do NOT generate destructive attack scripts or automated exploit toolchains aimed at damaging un-authorized systems.
                """.trimIndent()
            )
        )
    )

    suspend fun sendMessage(
        history: List<ChatMessage>,
        newMessageText: String,
        customApiKey: String? = null,
        temperature: Float = 0.7f
    ): String = withContext(Dispatchers.IO) {
        val apiKey = if (!customApiKey.isNullOrBlank()) {
            customApiKey.trim()
        } else {
            try {
                BuildConfig.GEMINI_API_KEY
            } catch (e: Exception) {
                ""
            }
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
            generationConfig = GenerationConfig(temperature = temperature)
        )

        // If API key is present and non-empty, try candidate Gemini models
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            for (model in CANDIDATE_MODELS) {
                try {
                    val response = apiService.generateContent(model, apiKey, request)
                    val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!reply.isNullOrBlank()) {
                        return@withContext reply
                    }
                } catch (e: Exception) {
                    // Try next model candidate
                }
            }
        }

        // If API Key is missing or all online calls failed, fall back to BugBountyKnowledgeBase
        val fallbackResponse = BugBountyKnowledgeBase.generateExpertResponse(newMessageText)
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            "[Bug Bounty Expert Mode - Offline Engine Active]\n\n$fallbackResponse"
        } else {
            "[Note: Gemini API unavailable or quota reached. Serving from Bug Bounty Expert Engine]\n\n$fallbackResponse"
        }
    }

    suspend fun testApiKey(key: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val apiKey = key.trim()
        if (apiKey.isBlank()) {
            return@withContext Pair(false, "API Key is empty.")
        }

        val request = GeminiRequest(
            contents = listOf(Content(role = "user", parts = listOf(Part(text = "Hello, respond with OK."))))
        )

        var lastError = ""
        for (model in CANDIDATE_MODELS) {
            try {
                val response = apiService.generateContent(model, apiKey, request)
                val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!reply.isNullOrBlank()) {
                    return@withContext Pair(true, "API Key validated successfully using model $model! Response: $reply")
                }
            } catch (e: Exception) {
                lastError = e.localizedMessage ?: e.message ?: "Error testing model $model"
            }
        }

        Pair(false, "Validation failed across candidate models: $lastError")
    }
}
