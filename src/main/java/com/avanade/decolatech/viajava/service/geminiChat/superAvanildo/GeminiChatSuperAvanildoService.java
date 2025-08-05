package com.avanade.decolatech.viajava.service.geminiChat.superAvanildo;


import com.avanade.decolatech.viajava.config.chatbot.GeminiApiClient;
import com.avanade.decolatech.viajava.controller.GeminiChatController;
import com.avanade.decolatech.viajava.domain.dtos.request.geminiChat.GeminiChatHistoryRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeminiChatSuperAvanildoService {

    private final DataSnapshotService dataSnapshotService;
    private final GeminiApiClient geminiApiClient;

    @Value("classpath:prompts/data-persona.txt")
    private Resource personaResource;
    @Value("classpath:prompts/data-task-rules.txt")
    private Resource taskRulesResource;
    @Value("classpath:prompts/data-output-format.txt")
    private Resource outputFormatResource;

    private String systemInstruction;

    public GeminiChatSuperAvanildoService(DataSnapshotService dataSnapshotService, GeminiApiClient geminiApiClient) {
        this.dataSnapshotService = dataSnapshotService;
        this.geminiApiClient = geminiApiClient;
    }

    @PostConstruct
    public void init() {
        try {
            String persona = new String(personaResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String taskRules = new String(taskRulesResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String outputFormat = new String(outputFormatResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            this.systemInstruction = String.join("\n\n", persona, taskRules, outputFormat);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar os arquivos de prompt de dados.", e);
        }
    }

    public GeminiChatController.DataChatResponse getChatResponse(String userMessage, List<GeminiChatHistoryRequest> history) throws IOException {
        String dataContext = dataSnapshotService.getDatabaseSnapshotAsJson();
        String historyContext = formatHistory(history);

        String userPrompt = "Contexto de Dados do Banco de Dados: " + dataContext +
                "\n\nHistórico da Conversa Anterior:\n" + historyContext +
                "\n\nMensagem ATUAL do Usuário: \"" + userMessage + "\"";

        String rawGeminiResponse = geminiApiClient.generateContent(this.systemInstruction, userPrompt);

        return new GeminiChatController.DataChatResponse(rawGeminiResponse);
    }

    private String formatHistory(List<GeminiChatHistoryRequest> history) {
        if (history == null || history.isEmpty()) return "Nenhuma conversa anterior.";
        return history.stream()
                .map(msg -> ("assistente".equalsIgnoreCase(msg.sender()) ? "Assistente: " : "Usuário: ") + msg.text())
                .collect(Collectors.joining("\n"));
    }
}
