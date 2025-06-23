//package com.azure_trial.azure_Trial.Service;
//
//import com.azure.ai.inference.ChatCompletionsClient;
//import com.azure.ai.inference.ChatCompletionsClientBuilder;
//import com.azure.ai.inference.models.ChatChoice;
//import com.azure.ai.inference.models.ChatCompletions;
//import com.azure.ai.openai.models.ChatCompletionsOptions;
//import com.azure.ai.openai.models.ChatRequestMessage;
//import com.azure.ai.openai.models.ChatRequestSystemMessage;
//import com.azure.ai.openai.models.ChatRequestUserMessage;
//import com.azure.core.credential.AzureKeyCredential;
//import com.azure_trial.azure_Trial.AzureConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//
//
//
//@Service
//public class OpenAIService {
//
//    @Autowired
//    ChatCompletionsClient chatCompletionsClient;
//
//    @Autowired
//    AzureConfiguration config;
//
//    public String getRecommendation(String prompt){
//        String model = "gpt-4o";
//
//        ChatCompletionsClient client = new ChatCompletionsClientBuilder()
//                .credential(new AzureKeyCredential("<API_KEY>"))
//                .endpoint(config.)
//                .buildClient();
//
//        List<ChatRequestMessage> chatMessages = Arrays.asList(
//                new ChatRequestSystemMessage("You are a helpful assistant."),
//                new ChatRequestUserMessage(prompt)
//        );
//
//
//
//        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
//        chatCompletionsOptions.setMaxTokens(4096);
//        chatCompletionsOptions.setTemperature(1d);
//        chatCompletionsOptions.setTopP(1d);
//        chatCompletionsOptions.setModel(model);
//
//        ChatCompletions completions = chatCompletionsClient.complete();
//        StringBuilder response = new StringBuilder();
//        for (ChatChoice choice : completions.getChoices()) {
//            response.append(choice.getMessage().getContent()).append(" ");
//        }
//        return response.toString();
//    }
//}
