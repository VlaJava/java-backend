package com.avanade.decolatech.viajava.config.chatbot;

import com.avanade.decolatech.viajava.domain.dtos.response.PackageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class PromptManager {

    @Value("classpath:prompts/persona.txt")
    private Resource personaResource;
    @Value("classpath:prompts/task-rules.txt")
    private Resource taskRulesResource;
    @Value("classpath:prompts/output-format.txt")
    private Resource outputFormatResource;

    private String systemInstructionTemplate;

    @PostConstruct
    public void init() {
        try {
            String persona = new String(personaResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String taskRules = new String(taskRulesResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String outputFormat = new String(outputFormatResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            this.systemInstructionTemplate = String.join("\n\n", persona, taskRules, outputFormat);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar os arquivos de prompt.", e);
        }
    }

    public String getSystemInstruction() {
        return this.systemInstructionTemplate;
    }

    public String buildUserPrompt(List<PackageResponse> packages, String userMessage, String history) throws JsonProcessingException {
        String packageContext = new ObjectMapper().writeValueAsString(packages);
        return "Contexto de Pacotes Disponíveis: " + packageContext +
                "\n\nHistórico da Conversa Anterior:\n" + history +
                "\n\nMensagem ATUAL do Usuário: \"" + userMessage + "\"";
    }
}
