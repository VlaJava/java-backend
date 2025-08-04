package com.avanade.decolatech.viajava.domain.dtos.response.geminiChat;

import java.util.List;

public record GeminiChatResponse(String responseType, String content, List<String> packageIds) {}