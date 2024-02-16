package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.ClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ClientService {
    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);
    public static final String CLIENT_HOME_PATH = "/";
    public static final String CLIENT_PREMIUM_PATH = "/protected/premium";

    private final WebClient webClient;
    private final ClientConfiguration clientConfiguration;

    @Autowired
    public ClientService(ClientConfiguration clientConfiguration) {
        webClient = WebClient.builder()
                .baseUrl(clientConfiguration.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.clientConfiguration = clientConfiguration;
    }

    public void printClientConfiguration() {
        LOG.info("Client configuration: {}", clientConfiguration);
    }

    public String access(String url, String token) {
        LOG.debug("Access {} - token: {}", url, token);
        ResponseEntity<String> response = webClient.get()
                .uri(url)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .toEntity(String.class)
                .block();

        LOG.debug("Status {} - Response: {}", response.getStatusCode(), response.getBody());
        return response.getBody();
    }
}
