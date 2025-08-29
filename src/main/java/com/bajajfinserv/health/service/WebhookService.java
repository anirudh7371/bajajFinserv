package com.bajajfinserv.health.service;

import com.bajajfinserv.health.model.SolutionRequest;
import com.bajajfinserv.health.model.WebhookRequest;
import com.bajajfinserv.health.model.WebhookResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebhookService {

    private final WebClient webClient;
    private static final String BASE_URL = "https://bfhldevapigw.healthrx.co.in/hiring";

    public WebhookService() {
        this.webClient = WebClient.builder().build();
    }

    public Mono<WebhookResponse> generateWebhook(WebhookRequest request) {
        return webClient.post()
                .uri(BASE_URL + "/generateWebhook/JAVA")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(WebhookResponse.class);
    }

    public Mono<String> submitSolution(String webhookUrl, String accessToken, SolutionRequest solution) {
        return webClient.post()
                .uri(webhookUrl)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(solution)
                .retrieve()
                .bodyToMono(String.class);
    }
}