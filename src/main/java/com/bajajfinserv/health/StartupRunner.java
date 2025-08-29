package com.bajajfinserv.health;

import com.bajajfinserv.health.model.SolutionRequest;
import com.bajajfinserv.health.model.WebhookRequest;
import com.bajajfinserv.health.service.SqlService;
import com.bajajfinserv.health.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private SqlService sqlService;

    @Override
    public void run(String... args) {
        try {
            System.out.println("Starting Bajaj Finserv Health Application");

            WebhookRequest request = new WebhookRequest("John Doe", "REG12347", "john@example.com");

            webhookService.generateWebhook(request)
                    .flatMap(response -> {
                        System.out.println("Webhook generated successfully!");
                        System.out.println("Webhook URL: " + response.getWebhook());
                        System.out.println("Access Token: " + response.getAccessToken());

                        String sqlQuery = sqlService.getQuery();
                        System.out.println("Generated SQL Query: " + sqlQuery);

                        SolutionRequest solution = new SolutionRequest(sqlQuery);

                        return webhookService.submitSolution(response.getWebhook(), response.getAccessToken(), solution)
                                .doOnSuccess(result -> {
                                    System.out.println("Solution submitted successfully!");
                                    System.out.println("Response: " + result);
                                })
                                .doOnError(error -> {
                                    System.err.println("❌ Error submitting solution: " + error.getMessage());
                                    error.printStackTrace();
                                });
                    })
                    .doOnError(error -> {
                        System.err.println("❌ Error generating webhook: " + error.getMessage());
                        error.printStackTrace();
                    })
                    .block();

        } catch (Exception e) {
            System.err.println("❌ Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}