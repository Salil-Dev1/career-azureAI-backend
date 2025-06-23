package com.azure_trial.azure_Trial;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.azure.ai.vision.imageanalysis.*;
import com.azure.ai.vision.imageanalysis.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.polling.SyncPoller;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.AnalyzeResultOperation;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentPage;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentTable;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;

import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.KeyCredential;
import com.azure.core.credential.KeyCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import static com.azure.ai.openai.models.OnYourDataAuthenticationType.CONNECTION_STRING;


@Configuration
public class AzureConfiguration {

    //Text Analytics Configuration
    @Value("${AZURE_SERVICES_KEY:default_value}")
    private String azureKey;

    @Value("${AZURE_SERVICES_ENDPOINT:default_value}")
    private String azureEndpoint;

    @Bean
    public TextAnalyticsClient textAnalyticsClient(){
        System.out.println("Azure Key: "+azureKey);
        System.out.println("Azure Key: "+azureEndpoint);
        return new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(azureKey)).endpoint(azureEndpoint)
                .buildClient();
    }



    @Bean
    public DocumentAnalysisClient documentAnalysisClient() {
        return new DocumentAnalysisClientBuilder()
                .credential(new AzureKeyCredential(azureKey))
                .endpoint(azureEndpoint)
                .buildClient();
    }



    @Bean
    public ImageAnalysisClient imageAnalysisClient(){
        return new ImageAnalysisClientBuilder().endpoint(azureEndpoint)
                .credential(new KeyCredential(azureKey))
                .buildClient();
    }



    //Open AI Configuration
    @Value("${OPENAI_KEY:default_value}")
    private String openAiKey;

    @Value("${OPENAI_ENDPOINT:default_value}")
    private String openAiEndpoint;



    @Bean
    public ChatCompletionsClient chatCompletionsClient(){
        return new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(openAiKey))
                .endpoint(openAiEndpoint)
                .buildClient();
    }

}
