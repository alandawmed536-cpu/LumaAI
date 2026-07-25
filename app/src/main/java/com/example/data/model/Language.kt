package com.example.data.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flagEmoji: String,
    val systemPromptInstruction: String
) {
    ENGLISH(
        code = "en",
        displayName = "English",
        nativeName = "English",
        flagEmoji = "🇬🇧",
        systemPromptInstruction = "Default primary language is English. Respond concisely and intelligently in English unless the user writes in another language."
    ),
    KURDISH_SORANI(
        code = "ckb",
        displayName = "Kurdish (Sorani)",
        nativeName = "کوردی (سۆرانی)",
        flagEmoji = "☀️",
        systemPromptInstruction = "Primary language for user responses should be Central Kurdish (Sorani / کوردیی سۆرانی) using Kurdish Arabic script. Respond naturally, accurately, and politely in Kurdish."
    ),
    KURDISH_BADINI(
        code = "ku",
        displayName = "Kurdish (Kurmanji/Badini)",
        nativeName = "Kurdî (کوردیا ژۆرین)",
        flagEmoji = "☀️",
        systemPromptInstruction = "Primary language for user responses should be Kurdish Kurmanji/Badini (کوردیا ژۆرین / Kurdî). Respond accurately and naturally."
    ),
    ARABIC(
        code = "ar",
        displayName = "Arabic",
        nativeName = "العربية",
        flagEmoji = "🇸🇦",
        systemPromptInstruction = "Primary language for user responses should be Arabic (العربية). Respond eloquently, accurately, and politely."
    ),
    FRENCH(
        code = "fr",
        displayName = "French",
        nativeName = "Français",
        flagEmoji = "🇫🇷",
        systemPromptInstruction = "Primary language for user responses should be French (Français). Respond in elegant, precise French."
    ),
    GERMAN(
        code = "de",
        displayName = "German",
        nativeName = "Deutsch",
        flagEmoji = "🇩🇪",
        systemPromptInstruction = "Primary language for user responses should be German (Deutsch). Respond accurately and clearly."
    ),
    SPANISH(
        code = "es",
        displayName = "Spanish",
        nativeName = "Español",
        flagEmoji = "🇪🇸",
        systemPromptInstruction = "Primary language for user responses should be Spanish (Español). Respond fluently and helpfully."
    ),
    TURKISH(
        code = "tr",
        displayName = "Turkish",
        nativeName = "Türkçe",
        flagEmoji = "🇹🇷",
        systemPromptInstruction = "Primary language for user responses should be Turkish (Türkçe). Respond fluently and clearly."
    ),
    JAPANESE(
        code = "ja",
        displayName = "Japanese",
        nativeName = "日本語",
        flagEmoji = "🇯🇵",
        systemPromptInstruction = "Primary language for user responses should be Japanese (日本語). Respond politely and accurately."
    )
}
