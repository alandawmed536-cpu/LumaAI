package com.example.data.repository

import com.example.BuildConfig
import com.example.data.local.ChatDao
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatSessionEntity
import com.example.data.model.AppLanguage
import com.example.data.remote.Content
import com.example.data.remote.GenerateContentRequest
import com.example.data.remote.GenerationConfig
import com.example.data.remote.Part
import com.example.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.UUID

class LumaRepository(private val chatDao: ChatDao) {

    val allSessions: Flow<List<ChatSessionEntity>> = chatDao.getAllSessions()

    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessageEntity>> {
        return chatDao.getMessagesForSession(sessionId)
    }

    suspend fun createNewSession(title: String = "New Chat", languageCode: String = "en"): String {
        val newId = UUID.randomUUID().toString()
        val session = ChatSessionEntity(
            id = newId,
            title = title,
            createdAt = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis(),
            languageCode = languageCode
        )
        chatDao.insertSession(session)
        return newId
    }

    suspend fun deleteSession(sessionId: String) {
        chatDao.deleteMessagesForSession(sessionId)
        chatDao.deleteSession(sessionId)
    }

    suspend fun clearAllHistory() {
        chatDao.clearAllMessages()
        chatDao.clearAllSessions()
    }

    suspend fun updateSessionTitle(sessionId: String, title: String) {
        val existing = chatDao.getSessionById(sessionId) ?: return
        chatDao.insertSession(existing.copy(title = title, lastUpdated = System.currentTimeMillis()))
    }

    suspend fun sendMessage(
        sessionId: String,
        userText: String,
        selectedLanguage: AppLanguage,
        modelName: String = "gemini-3.5-flash",
        temperature: Float = 0.7f,
        onChunk: (suspend (String) -> Unit)? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                Exception("Gemini API Key is missing. Please configure your API key in the Secrets panel in AI Studio.")
            )
        }

        // Save User Message to Database
        val userMsgEntity = ChatMessageEntity(
            sessionId = sessionId,
            text = userText,
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(userMsgEntity)

        // Update Session Title if it's the first user message
        val currentMessages = chatDao.getMessagesForSession(sessionId).first()
        if (currentMessages.size <= 1 || currentMessages.all { !it.isFromUser || it.text == userText }) {
            val titleSnippet = if (userText.length > 30) userText.take(28) + "..." else userText
            updateSessionTitle(sessionId, titleSnippet)
        }

        // Build System Instruction with Creator Info and Language Customization
        val systemInstructionText = """
            You are LumaAI, an exceptionally intelligent, precise, helpful, and polite AI assistant.
            
            [CRITICAL CREATOR INFORMATION]:
            When asked about your creator, developer, or who made/built you (e.g., in Kurdish 'کێ دروستی کردووی', 'دروستکەرەکەت کێیە', in English 'Who created you?', 'Who built you?', 'Who made you?', in Arabic 'من صنعك', or any other language), you MUST ALWAYS clearly state that you were created by "Kurdish Co" ("لە لایەن کوردی کە وە دروست کراوە" in Kurdish / "Created by Kurdish Co" in English).
            
            [LANGUAGE SETTING]:
            ${selectedLanguage.systemPromptInstruction}
            
            Maintain a warm, smart, and well-structured response style with clear bullet points, accurate code syntax highlighting when applicable, and helpful explanations.
        """.trimIndent()

        // Build Conversation History for Gemini API
        val contentsList = mutableListOf<Content>()
        // Include previous messages for context (up to last 10 messages)
        val contextMessages = currentMessages.takeLast(10)
        for (msg in contextMessages) {
            val role = if (msg.isFromUser) "user" else "model"
            contentsList.add(
                Content(
                    role = role,
                    parts = listOf(Part(text = msg.text))
                )
            )
        }

        val requestBody = GenerateContentRequest(
            contents = contentsList,
            generationConfig = GenerationConfig(temperature = temperature),
            systemInstruction = Content(parts = listOf(Part(text = systemInstructionText)))
        )

        // Create empty AI message entity in database to stream into or populate
        val aiMsgId = chatDao.insertMessage(
            ChatMessageEntity(
                sessionId = sessionId,
                text = "",
                isFromUser = false,
                timestamp = System.currentTimeMillis()
            )
        )

        try {
            if (onChunk != null) {
                // Streaming mode
                val responseBody = RetrofitClient.service.streamGenerateContentFlash(apiKey, requestBody)
                val fullResponseBuilder = StringBuilder()

                responseBody.byteStream().bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val currentLine = line ?: continue
                        if (currentLine.startsWith("data: ") || currentLine.startsWith("{")) {
                            val jsonString = if (currentLine.startsWith("data: ")) currentLine.substring(6) else currentLine
                            try {
                                val json = JSONObject(jsonString)
                                val candidates = json.optJSONArray("candidates")
                                if (candidates != null && candidates.length() > 0) {
                                    val candidate = candidates.getJSONObject(0)
                                    val contentObj = candidate.optJSONObject("content")
                                    if (contentObj != null) {
                                        val partsArr = contentObj.optJSONArray("parts")
                                        if (partsArr != null && partsArr.length() > 0) {
                                            val partText = partsArr.getJSONObject(0).optString("text", "")
                                            if (partText.isNotEmpty()) {
                                                fullResponseBuilder.append(partText)
                                                val accumulatedText = fullResponseBuilder.toString()
                                                chatDao.updateMessageText(aiMsgId, accumulatedText)
                                                onChunk(accumulatedText)
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                // Ignore non-JSONSSE delimiter lines
                            }
                        }
                    }
                }

                val finalResultText = fullResponseBuilder.toString()
                if (finalResultText.isEmpty()) {
                    // Fallback to non-streaming if stream came back empty
                    val response = if (modelName.contains("pro")) {
                        RetrofitClient.service.generateContentPro(apiKey, requestBody)
                    } else {
                        RetrofitClient.service.generateContentFlash(apiKey, requestBody)
                    }
                    val extractedText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                        ?: "No response generated."
                    chatDao.updateMessageText(aiMsgId, extractedText)
                    Result.success(extractedText)
                } else {
                    Result.success(finalResultText)
                }
            } else {
                // Non-streaming standard call
                val response = if (modelName.contains("pro")) {
                    RetrofitClient.service.generateContentPro(apiKey, requestBody)
                } else {
                    RetrofitClient.service.generateContentFlash(apiKey, requestBody)
                }

                val extractedText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "No response generated."
                chatDao.updateMessageText(aiMsgId, extractedText)
                Result.success(extractedText)
            }
        } catch (e: Exception) {
            val errorText = "Error communicating with LumaAI: ${e.localizedMessage ?: e.message}"
            chatDao.updateMessageText(aiMsgId, errorText, isError = true)
            Result.failure(e)
        }
    }
}
