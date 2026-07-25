package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
fun SettingsDialog(
    selectedLanguage: AppLanguage,
    selectedModel: String,
    temperature: Float,
    onLanguageChange: (AppLanguage) -> Unit,
    onModelChange: (String) -> Unit,
    onTemperatureChange: (Float) -> Unit,
    onClearAllClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, CardDarkBorder, RoundedCornerShape(20.dp))
                .testTag("settings_dialog"),
            color = DeepDarkBg
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = CyanLuma,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Settings & LumaAI Info",
                            color = TextSlateWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMutedGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = CardDarkBorder)
                Spacer(modifier = Modifier.height(16.dp))

                // Kurdish Co Badge Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardDarkSurface
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(CyanLuma.copy(alpha = 0.5f), NeonKurdishGold.copy(alpha = 0.5f)))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(CyanLuma, PurpleLuma))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "☀️",
                                fontSize = 22.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "LumaAI Intelligence",
                                color = TextSlateWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Developed & Created by Kurdish Co",
                                color = NeonKurdishGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "لە لایەن کوردی کە وە دروست کراوە",
                                color = TextMutedGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Language Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = CyanLuma,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Primary Assistant Language",
                        color = TextSlateWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardDarkSurface)
                        .padding(8.dp)
                ) {
                    AppLanguage.entries.forEach { lang ->
                        val isSelected = lang == selectedLanguage
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyanLuma.copy(alpha = 0.15f) else Color.Transparent)
                                .clickable { onLanguageChange(lang) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = lang.flagEmoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "${lang.displayName} (${lang.nativeName})",
                                    color = if (isSelected) CyanLuma else TextSlateWhite,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = CyanLuma,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // AI Model Selection Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = CyanLuma,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Model Engine",
                        color = TextSlateWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val models = listOf(
                    "gemini-3.5-flash" to "Luma 3.5 Flash (Ultra Fast & Smart)",
                    "gemini-3.1-pro-preview" to "Luma 3.1 Pro (Deep Reasoning)"
                )

                models.forEach { (modelKey, label) ->
                    val isSelected = modelKey == selectedModel
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) CardDarkSurface else PitchBlack)
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) CyanLuma else CardDarkBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onModelChange(modelKey) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) CyanLuma else TextSlateWhite,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(CyanLuma)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Temperature Slider Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = null,
                            tint = CyanLuma,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Creativity (Temperature)",
                            color = TextSlateWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = String.format("%.1f", temperature),
                        color = CyanLuma,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Slider(
                    value = temperature,
                    onValueChange = onTemperatureChange,
                    valueRange = 0.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = CyanLuma,
                        activeTrackColor = CyanLuma,
                        inactiveTrackColor = CardDarkBorder
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = CardDarkBorder)
                Spacer(modifier = Modifier.height(16.dp))

                // Actions: Clear History
                OutlinedButton(
                    onClick = {
                        onClearAllClick()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("clear_history_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEF4444)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder().copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFEF4444)))
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear History",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Clear All Chat History",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
