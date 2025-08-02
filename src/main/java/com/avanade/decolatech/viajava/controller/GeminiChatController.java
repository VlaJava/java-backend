package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.geminiChat.GeminiChatRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.geminiChat.BotResponse;
import com.avanade.decolatech.viajava.service.geminiChat.GeminiChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class GeminiChatController {

    private final GeminiChatService geminiChatService;

    public GeminiChatController(GeminiChatService geminiChatService) {
        this.geminiChatService = geminiChatService;
    }

    @PostMapping
    public ResponseEntity<BotResponse> handleChatMessage(@Valid @RequestBody GeminiChatRequest request) throws Exception {

        BotResponse response = geminiChatService.getChatResponse(request.message(), request.history());
        return ResponseEntity.ok(response);
    }
}