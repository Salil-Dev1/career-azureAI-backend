package com.microsoft.models.inference.samples;
import java.util.Arrays;
import java.util.List;
import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.Configuration;

public final class BasicChatSample {
    public static void main(String[] args) {
        String endpoint = "https://career-recommender-hackathon-ai.openai.azure.com/openai/deployments/gpt-4o";
        String model = "gpt-4o";

        ChatCompletionsClient client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential("<API key>"))
                .endpoint(endpoint)
                .buildClient();

        List<ChatRequestMessage> chatMessages = Arrays.asList(
                new ChatRequestSystemMessage("You are a helpful assistant."),
                new ChatRequestUserMessage("I want to learn Java, how to learn it?")
        );

        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
        chatCompletionsOptions.setMaxTokens(4096);
        chatCompletionsOptions.setTemperature(1d);
        chatCompletionsOptions.setTopP(1d);
        chatCompletionsOptions.setModel(model);

        ChatCompletions completions = client.complete(chatCompletionsOptions);
        for (ChatChoice choice : completions.getChoices()) {
            System.out.printf("%s.%n", choice.getMessage().getContent());
        }
    }
}
