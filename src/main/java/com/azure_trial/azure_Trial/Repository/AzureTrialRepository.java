package com.azure_trial.azure_Trial.Repository;

import com.azure_trial.azure_Trial.Models.AzureTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AzureTrialRepository extends JpaRepository<AzureTrial, Long> {
}
