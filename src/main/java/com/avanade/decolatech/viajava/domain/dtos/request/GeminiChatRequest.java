package com.avanade.decolatech.viajava.domain.dtos.request;

import java.util.List;

public record GeminiChatRequest(String message, List<GeminiChatHistoryRequest> history) {}