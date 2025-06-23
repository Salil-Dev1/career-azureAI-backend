package com.azure_trial.azure_Trial.Controller;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.core.annotation.Post;
import com.azure_trial.azure_Trial.Models.AzureTrial;
import com.azure_trial.azure_Trial.Service.AzureTrialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "*", maxAge = 4800)
@RestController
@RequestMapping("/analyze")
public class AzureController {

    @Autowired
    AzureTrialService azureTrialService;


    @GetMapping
    public ResponseEntity<List<AzureTrial>> getData() {
        return ResponseEntity.ok(azureTrialService.getCareerRecommendation());
    }

    @PostMapping
    public ResponseEntity<String> enterString(@RequestBody AzureTrial azureTrial) {
        return ResponseEntity.ok(azureTrialService.enterData(azureTrial));
    }

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file")MultipartFile file){
        try{
            return azureTrialService.processDocument(file);
        } catch (IOException e){
            return "Failed to process the document : " + e.getMessage();
        }
    }

}
