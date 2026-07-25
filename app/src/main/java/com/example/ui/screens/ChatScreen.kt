package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatMessageEntity
import com.example.data.model.AppLanguage
import com.example.ui.theme.AiBubbleColor
import com.example.ui.theme.CardDarkBorder
import com.example.ui.theme.CardDarkSurface
import com.example.ui.theme.CyanLuma
import com.example.ui.theme.DeepDarkBg
import com.example.ui.theme.ErrorBubbleColor
import com.example.ui.theme.NeonKurdishGold
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.PurpleLuma
import com.example.ui.theme.TextDimmed
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextSlateWhite
import com.example.ui.theme.UserBubbleColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<ChatMessageEntity>,
    inputText: String,
    isGenerating: Boolean,
    selectedLanguage: AppLanguage,
    selectedModel: String,
    onInputTextChange: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    onOpenDrawer: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // Speech-To-Text Launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
        if (!spokenText.isNullOrBlank()) {
            onInputTextChange(spokenText)
        }
    }

    // Auto-scroll to bottom on new message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(CyanLuma, PurpleLuma))
                                )
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(PitchBlack),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "L",
                                    color = CyanLuma,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "LumaAI",
                                    color = TextSlateWhite,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CyanLuma.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = selectedLanguage.flagEmoji,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Text(
                                text = "Created by Kurdish Co",
                                color = NeonKurdishGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier.testTag("open_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Drawer",
                            tint = TextSlateWhite
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.testTag("open_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = CyanLuma
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PitchBlack
                )
            )
        },
        containerColor = PitchBlack,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Chat Content Canvas
            if (messages.isEmpty()) {
                // Welcome Screen
                EmptyWelcomeCanvas(
                    selectedLanguage = selectedLanguage,
                    onPromptSelect = { prompt -> onSendMessage(prompt) },
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        MessageItem(
                            message = msg,
                            onCopyText = {
                                clipboardManager.setText(AnnotatedString(msg.text))
                            }
                        )
                    }

                    if (isGenerating) {
                        item {
                            TypingIndicatorItem()
                        }
                    }
                }
            }

            // Input Bar
            InputBottomBar(
                inputText = inputText,
                isGenerating = isGenerating,
                selectedLanguage = selectedLanguage,
                onInputTextChange = onInputTextChange,
                onSend = { onSendMessage(null) },
                onMicClick = {
                    try {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to LumaAI...")
                        }
                        speechLauncher.launch(intent)
                    } catch (e: Exception) {
                        // Voice recognizer fallback if unavailable
                    }
                }
            )
        }
    }
}

@Composable
fun MessageItem(
    message: ChatMessageEntity,
    onCopyText: () -> Unit
) {
    val isUser = message.isFromUser

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!isUser) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(CyanLuma, PurpleLuma))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "LumaAI",
                        tint = PitchBlack,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                color = if (message.isError) ErrorBubbleColor
                        else if (isUser) UserBubbleColor
                        else AiBubbleColor,
                border = if (!isUser) androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(listOf(CyanLuma.copy(alpha = 0.4f), Color.Transparent))
                ) else null,
                modifier = Modifier
                    .fillMaxWidth(if (isUser) 0.82f else 0.88f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = message.text.ifBlank { "..." },
                        color = TextSlateWhite,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isUser) "You" else "LumaAI",
                            color = if (isUser) TextMutedGray else CyanLuma,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = onCopyText,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = TextDimmed,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicatorItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(CyanLuma, PurpleLuma))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = null,
                tint = PitchBlack,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AiBubbleColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, CardDarkBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = CyanLuma,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "LumaAI is thinking...",
                    color = TextMutedGray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun EmptyWelcomeCanvas(
    selectedLanguage: AppLanguage,
    onPromptSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Glowing Hero Emblem
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(CyanLuma, PurpleLuma, NeonKurdishGold)
                    )
                )
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(PitchBlack),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Luma",
                    color = CyanLuma,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to LumaAI",
            color = TextSlateWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Creator Badge Banner
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CardDarkSurface,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Brush.horizontalGradient(listOf(CyanLuma, NeonKurdishGold))
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "☀️ ", fontSize = 14.sp)
                Text(
                    text = "Created by Kurdish Co",
                    color = NeonKurdishGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (selectedLanguage == AppLanguage.KURDISH_SORANI)
                "پڕسیارم لێ بکە دەربارەی هەر بابەتێک یان بپرسە کێ دروستی کردووم!"
            else if (selectedLanguage == AppLanguage.KURDISH_BADINI)
                "پڕسیاران ژ من بکە دەربارەی هەر تشتەکی یان بپرسە کێ ئەی ئەز دروستکریم!"
            else if (selectedLanguage == AppLanguage.ARABIC)
                "اسألني أي سؤال أو اسألني من قَام بتطويري!"
            else
                "Ask me anything or ask me who created me!",
            color = TextMutedGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Prompt Suggestion Chips
        val samplePrompts = listOf(
            "Who created you?" to "کێ دروستی کردووی؟",
            "Write a poem in Kurdish" to "شێعرێکی خۆش بە کوردی بنووسە",
            "Explain Quantum Computing" to "شیکردنەوەی فیزیا بە کورتی",
            "Generate Kotlin Compose code" to "کۆدی ئەندرۆید لەسەر کۆمپۆز"
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(samplePrompts) { (engPrompt, kurdPrompt) ->
                val displayPrompt = if (selectedLanguage == AppLanguage.KURDISH_SORANI || selectedLanguage == AppLanguage.KURDISH_BADINI) kurdPrompt else engPrompt
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CardDarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardDarkBorder),
                    modifier = Modifier.clickable { onPromptSelect(displayPrompt) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CyanLuma,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = displayPrompt,
                            color = TextSlateWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputBottomBar(
    inputText: String,
    isGenerating: Boolean,
    selectedLanguage: AppLanguage,
    onInputTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onMicClick: () -> Unit
) {
    Surface(
        color = PitchBlack,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardDarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onMicClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CardDarkSurface)
                    .testTag("voice_dictation_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Input",
                    tint = CyanLuma,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = onInputTextChange,
                placeholder = {
                    Text(
                        text = if (selectedLanguage == AppLanguage.KURDISH_SORANI) "پەیامێک بنووسە بۆ LumaAI..."
                               else if (selectedLanguage == AppLanguage.KURDISH_BADINI) "ئاخفتنەکێ بنڤیسە بۆ LumaAI..."
                               else if (selectedLanguage == AppLanguage.ARABIC) "اكتب رسالتك لـ LumaAI..."
                               else "Ask LumaAI anything...",
                        color = TextDimmed,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("message_input_field"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardDarkSurface,
                    unfocusedContainerColor = CardDarkSurface,
                    focusedBorderColor = CyanLuma,
                    unfocusedBorderColor = CardDarkBorder,
                    focusedTextColor = TextSlateWhite,
                    unfocusedTextColor = TextSlateWhite
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSend,
                enabled = inputText.isNotBlank() && !isGenerating,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (inputText.isNotBlank() && !isGenerating) CyanLuma else CardDarkBorder
                    )
                    .testTag("send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (inputText.isNotBlank() && !isGenerating) PitchBlack else TextDimmed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
