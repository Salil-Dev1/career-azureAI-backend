package com.azure_trial.azure_Trial.Service;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.models.ChatChoice;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.CategorizedEntity;

import com.azure.ai.textanalytics.models.KeyPhrasesCollection;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.azure_trial.azure_Trial.Models.AzureTrial;
import com.azure_trial.azure_Trial.Repository.AzureTrialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.AnalyzeResultOperation;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentPage;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentTable;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AzureTrialService {


    @Autowired
    ChatCompletionsClient chatCompletionsClient;

    @Autowired
    AzureTrialRepository azureRepository;

    @Autowired
    AzureTrial azureTrial;

    @Autowired
    private TextAnalyticsClient textAnalyticsClient;

    @Autowired
    DocumentAnalysisClient documentAnalysisClient;

    String filePath = "C:\\Users\\Salil bhatt\\Desktop\\ai-response.txt";

    String query = "";

    private final DocumentAnalysisClient client;


    public AzureTrialService(DocumentAnalysisClient client) {
        this.client = client;
    }


    public List<AzureTrial> getCareerRecommendation(){
        return (List<AzureTrial>) azureRepository.findAll();
    }


    public String getRecommendation(String prompt){
        String model = "gpt-4o";

        List<com.azure.ai.inference.models.ChatRequestMessage> chatMessages = Arrays.asList(
                new com.azure.ai.inference.models.ChatRequestSystemMessage("You are a helpful assistant."),
                new com.azure.ai.inference.models.ChatRequestUserMessage(prompt)
        );

        com.azure.ai.inference.models.ChatCompletionsOptions chatCompletionsOptions = new com.azure.ai.inference.models.ChatCompletionsOptions(chatMessages);
        chatCompletionsOptions.setMaxTokens(4096);
        chatCompletionsOptions.setTemperature(1d);
        chatCompletionsOptions.setTopP(1d);
        chatCompletionsOptions.setModel(model);
        ChatCompletions completions = chatCompletionsClient.complete(chatCompletionsOptions);
        StringBuilder response = new StringBuilder();
        for (ChatChoice choice : completions.getChoices()) {
            response.append(choice.getMessage().getContent()).append(" ");
        }
        return response.toString();
    }

    public String enterData(AzureTrial azureTrial){
        try{
            String senti = textAnalyticsClient.analyzeSentiment(azureTrial.getInput()).getSentiment().toString();
            azureTrial.setSentiment(senti);

            if(azureTrial.getExtractedWords() == null){
                azureTrial.setExtractedWords(new ArrayList<>());
            }


            textAnalyticsClient.extractKeyPhrases(azureTrial.getInput()).forEach(keyPhrase -> {
                azureTrial.getExtractedWords().add(keyPhrase);
            });
            List<String> extractedData = azureTrial.getExtractedWords();
            query = "Based on the following skills and interests: " + String.join(",",
                    extractedData) + ", recommend suitable career paths.";
            azureTrial.setPrompt(query);
            azureRepository.save(azureTrial);


            // Get the AI recommendation
            String aiResponse = getRecommendation(query);
            return aiResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while generating recommendation: " + e.getMessage());
        }
        }

    public String processDocument(MultipartFile file) throws IOException{
        if (!file.getContentType().equals("application/pdf")) {
            return "Invalid file type. Only PDFs are supported.";
        }

        Path tempFile = Files.createTempFile("uploaded-", ".pdf");
        file.transferTo(tempFile.toFile());

        // Convert the file to byte array
        byte[] fileData = Files.readAllBytes(tempFile);
        BinaryData binaryData = BinaryData.fromBytes(fileData);

        // Pass the data to Azure Form Recognizer
        SyncPoller<OperationResult, AnalyzeResult> poller =
                client.beginAnalyzeDocument("prebuilt-document", binaryData);
        AnalyzeResult analyzeResult = poller.getFinalResult();

        // Build the result output
        StringBuilder results = new StringBuilder();
        analyzeResult.getPages().forEach(page -> {
            results.append(String.format("Page width: %.2f, height: %.2f, unit: %s%n",
                    page.getWidth(), page.getHeight(), page.getUnit()));
            page.getLines().forEach(line ->
                    results.append(String.format("Line: %s%n", line.getContent())));
        });

        if(analyzeResult.getTables() != null){
            analyzeResult.getTables().forEach(table -> {
                results.append(String.format("Table has %d rows and %d columns.%n", table.getRowCount(), table.getColumnCount()));
                table.getCells().forEach(cell ->
                        results.append(String.format("Cell content: %s, row: %d, column: %d%n",
                                cell.getContent(), cell.getRowIndex(), cell.getColumnIndex())));
            });
        }
        else{
            results.append("There are no tables in the document");
        }
        String finalResults = results.toString();
        String key_Words_From_Document = extractKeysFromDocuments(finalResults);

        return key_Words_From_Document;

    }

    public String extractKeysFromDocuments(String finalResults){
        List<String> keyValues = new ArrayList<>();
        textAnalyticsClient.extractKeyPhrases(finalResults).forEach(keyPhrase -> {
            keyValues.add(keyPhrase);
        });
        azureTrial.setExtractedWordsFromDocuments(keyValues);
        List<CategorizedEntity> nerEntities = new ArrayList<>();
        for (String keyword : keyValues) {
            textAnalyticsClient.recognizeEntities(keyword).forEach(nerEntities::add);
        }
        StringBuilder str = new StringBuilder();
        for(CategorizedEntity entity : nerEntities)
        {
            System.out.println("Entity: " + entity.getText() + ", Category: " + entity.getCategory());
           if(entity.getCategory() != null && entity.getCategory().toString().equalsIgnoreCase("Skill"))
            {

                str.append(entity.getText())
                        .append(" ");
            }
        }
        query = "Based on the following skills and interests: " + str.toString()
                 + ", recommend suitable career paths.";
        return getRecommendation(query);

    }


}



