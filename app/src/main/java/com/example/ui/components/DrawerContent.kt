package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatSessionEntity
import com.example.data.model.AppLanguage
import com.example.ui.theme.CardDarkBorder
import com.example.ui.theme.CardDarkSurface
import com.example.ui.theme.CyanLuma
import com.example.ui.theme.DeepDarkBg
import com.example.ui.theme.NeonKurdishGold
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.PurpleLuma
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextSlateWhite

@Composable
fun DrawerContent(
    sessions: List<ChatSessionEntity>,
    currentSessionId: String?,
    selectedLanguage: AppLanguage,
    onSessionSelect: (String) -> Unit,
    onNewChatClick: () -> Unit,
    onDeleteSessionClick: (String) -> Unit,
    onLanguageSelect: (AppLanguage) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp),
        color = DeepDarkBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            // Header Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(CyanLuma, PurpleLuma)
                            )
                        )
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(PitchBlack),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L",
                            color = CyanLuma,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "LumaAI",
                        color = TextSlateWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NeonKurdishGold)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Created by Kurdish Co",
                            color = NeonKurdishGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // New Chat Button
            Button(
                onClick = onNewChatClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("drawer_new_chat_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanLuma,
                    contentColor = PitchBlack
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Chat",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedLanguage == AppLanguage.KURDISH_SORANI) "گفتوگۆی نوێ"
                           else if (selectedLanguage == AppLanguage.KURDISH_BADINI) "ئاخفتنا نوو"
                           else if (selectedLanguage == AppLanguage.ARABIC) "محادثة جديدة"
                           else "New Chat",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Language Switcher Section Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = CyanLuma,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (selectedLanguage == AppLanguage.KURDISH_SORANI) "زمانەکان (Menu Language)"
                           else if (selectedLanguage == AppLanguage.KURDISH_BADINI) "زمان (Menu Language)"
                           else if (selectedLanguage == AppLanguage.ARABIC) "اللغات (Language)"
                           else "Menu Language",
                    color = TextMutedGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Language Horizontal Options Carousel
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardDarkSurface)
                    .padding(8.dp)
            ) {
                items(AppLanguage.entries.toTypedArray()) { lang ->
                    val isSelected = lang == selectedLanguage
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CyanLuma.copy(alpha = 0.15f) else Color.Transparent)
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) CyanLuma else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onLanguageSelect(lang) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = lang.flagEmoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = lang.nativeName,
                                color = if (isSelected) CyanLuma else TextSlateWhite,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(CyanLuma)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CardDarkBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Chat History Section Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "History",
                    tint = TextMutedGray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (selectedLanguage == AppLanguage.KURDISH_SORANI) "مێژووی گفتوگۆکان"
                           else if (selectedLanguage == AppLanguage.KURDISH_BADINI) "دیژۆکا ئاخفتنان"
                           else if (selectedLanguage == AppLanguage.ARABIC) "سجل المحادثات"
                           else "Recent Conversations",
                    color = TextMutedGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sessions List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(sessions) { session ->
                    val isSelected = session.id == currentSessionId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) CardDarkSurface else Color.Transparent
                            )
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) CyanLuma.copy(alpha = 0.5f) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onSessionSelect(session.id) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = null,
                                tint = if (isSelected) CyanLuma else TextMutedGray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = session.title,
                                color = if (isSelected) TextSlateWhite else TextMutedGray,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }

                        IconButton(
                            onClick = { onDeleteSessionClick(session.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Chat",
                                tint = TextMutedGray.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = CardDarkBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Settings & Developer Footer Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardDarkSurface)
                    .clickable { onOpenSettings() }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = CyanLuma,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (selectedLanguage == AppLanguage.KURDISH_SORANI) "ڕێکخستنەکان (Settings)"
                               else if (selectedLanguage == AppLanguage.KURDISH_BADINI) "ڕێکخستن (Settings)"
                               else if (selectedLanguage == AppLanguage.ARABIC) "الإعدادات (Settings)"
                               else "Settings & Model",
                        color = TextSlateWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = NeonKurdishGold,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
