package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.geminiChat.GeminiChatRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.geminiChat.BotResponse;
import com.avanade.decolatech.viajava.service.geminiChat.superAvanildo.GeminiChatSuperAvanildoService;
import com.avanade.decolatech.viajava.service.geminiChat.avanildo.GeminiChatAvanildoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class GeminiChatController {

    private final GeminiChatAvanildoService geminiChatAvanildoService;
    private final GeminiChatSuperAvanildoService geminiChatSuperAvanildoService;

    public GeminiChatController(GeminiChatAvanildoService geminiChatAvanildoService, GeminiChatSuperAvanildoService geminiChatSuperAvanildoService) {
        this.geminiChatSuperAvanildoService = geminiChatSuperAvanildoService;
        this.geminiChatAvanildoService = geminiChatAvanildoService;
    }

    public record DataChatResponse(String text) {}

    @PostMapping
    public ResponseEntity<BotResponse> handleChatMessage(@Valid @RequestBody GeminiChatRequest request) throws Exception {

        BotResponse response = geminiChatAvanildoService.getChatResponse(request.message(), request.history());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/super")
    public ResponseEntity<DataChatResponse> handleDataChatMessage(@Valid @RequestBody GeminiChatRequest request) throws Exception {
        DataChatResponse response = geminiChatSuperAvanildoService.getChatResponse(request.message(), request.history());
        return ResponseEntity.ok(response);
    }
}