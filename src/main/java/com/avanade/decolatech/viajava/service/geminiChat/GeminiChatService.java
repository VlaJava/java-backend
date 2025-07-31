package com.avanade.decolatech.viajava.service.geminiChat;

import com.avanade.decolatech.viajava.config.chatbot.GeminiApiClient;
import com.avanade.decolatech.viajava.config.chatbot.PromptManager;
import com.avanade.decolatech.viajava.domain.dtos.request.GeminiChatHistoryRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.BotResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.GeminiChatResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.pacote.PackageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GeminiChatService {

    private final GetAllPackageChatService packageService;
    private final PromptManager promptManager;
    private final GeminiApiClient geminiApiClient;
    private final ObjectMapper objectMapper;

    public GeminiChatService(GetAllPackageChatService packageService, PromptManager promptManager, GeminiApiClient geminiApiClient, ObjectMapper objectMapper) {
        this.packageService = packageService;
        this.promptManager = promptManager;
        this.geminiApiClient = geminiApiClient;
        this.objectMapper = objectMapper;
    }

    public BotResponse getChatResponse(String userMessage, List<GeminiChatHistoryRequest> history) throws IOException {
        List<PackageResponse> packages = packageService.getAllPackageChat();
        String historyContext = formatHistory(history);

        String systemInstruction = promptManager.getSystemInstruction();
        String userPrompt = promptManager.buildUserPrompt(packages, userMessage, historyContext);

        String rawGeminiResponse = geminiApiClient.generateContent(systemInstruction, userPrompt);

        return processGeminiResponse(rawGeminiResponse);
    }

    private BotResponse processGeminiResponse(String rawText) throws IOException {
        String cleanedText = rawText.replace("```json", "").replace("```", "").trim();

        if (cleanedText.startsWith("{")) {
            GeminiChatResponse geminiResponse = objectMapper.readValue(cleanedText, GeminiChatResponse.class);
            List<PackageResponse> recommendedPackages = new ArrayList<>();

            if ("RECOMMENDATION".equals(geminiResponse.responseType()) && geminiResponse.packageIds() != null && !geminiResponse.packageIds().isEmpty()) {
                for (String id : geminiResponse.packageIds()) {
                    try {
                        recommendedPackages.add(this.packageService.findById(UUID.fromString(id)));
                    } catch (Exception e) {
                        System.err.println("Pacote com ID " + id + " não encontrado ou ID inválido. Ignorando.");
                    }
                }
            }
            return new BotResponse(geminiResponse.content(), recommendedPackages);
        } else {
            return new BotResponse(cleanedText, new ArrayList<>());
        }
    }

    private String formatHistory(List<GeminiChatHistoryRequest> history) {
        if (history == null || history.isEmpty()) return "Nenhuma conversa anterior.";
        return history.stream()
                .map(msg -> ("assistente".equalsIgnoreCase(msg.sender()) ? "Assistente: " : "Usuário: ") + msg.text())
                .collect(Collectors.joining("\n"));
    }
}