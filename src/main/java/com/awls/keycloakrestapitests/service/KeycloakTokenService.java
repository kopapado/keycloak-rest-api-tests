package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.KeycloakConfiguration;
import com.awls.keycloakrestapitests.model.TokenDetails;
import io.netty.handler.timeout.WriteTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KeycloakTokenService {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakTokenService.class);

    private final KeycloakConfiguration keycloakConfiguration;
    private final WebClient webClient;

    @Autowired
    public KeycloakTokenService(KeycloakConfiguration keycloakConfiguration, WebClient webClient) {
        this.keycloakConfiguration = keycloakConfiguration;
        this.webClient = webClient;
    }

    public void printKeycloakConfiguration() {
        LOG.info("Keycloak configuration: {}", keycloakConfiguration);
    }

    public TokenDetails getAdminToken() {
        LOG.debug("Get Admin Token");
        BodyInserters.FormInserter<String> formInserter = BodyInserters
                .fromFormData("client_id", "admin-cli")
                .with("username", "admin")
                .with("password", "admin")
                .with("grant_type", "password");

        ResponseEntity<TokenDetails> response = webClient.post()
                .uri("/realms/master/protocol/openid-connect/token")
                .body(formInserter)
                .retrieve()
                .toEntity(TokenDetails.class)
                .doOnError(WriteTimeoutException.class, ex -> LOG.error("Get Token Timeout"))
                .block();

        LOG.debug("Status: {}, Token Details: {}", response.getStatusCode(), response.getBody());
        return response.getBody();
    }
}
