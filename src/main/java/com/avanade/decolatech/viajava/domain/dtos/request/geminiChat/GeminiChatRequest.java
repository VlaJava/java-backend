package com.avanade.decolatech.viajava.domain.dtos.request.geminiChat;

import java.util.List;

public record GeminiChatRequest(String message, List<GeminiChatHistoryRequest> history) {}