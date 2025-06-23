package com.azure_trial.azure_Trial.Models;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@Entity
@Table(name = "sentiment")
@Component
public class AzureTrial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String input;
    private String sentiment;
    private List<String> extractedWords;
    private List<String> extractedWordsFromDocuments;


    private String prompt;



    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }


    public List<String> getExtractedWords() {
        return extractedWords;
    }

    public void setExtractedWords(List<String> extractedWords) {
        this.extractedWords = extractedWords;
    }


    public List<String> getExtractedWordsFromDocuments() {
        return extractedWordsFromDocuments;
    }

    public void setExtractedWordsFromDocuments(List<String> extractedWordsFromDocuments) {
        this.extractedWordsFromDocuments = extractedWordsFromDocuments;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }



}
