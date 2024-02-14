package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.model.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KeycloakUserService {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserService.class);

    private final WebClient webClient;

    @Autowired
    public KeycloakUserService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void createUser(UserRepresentation userRepresentation, String token) {
        LOG.debug("Create user - user: {}, token: {}", userRepresentation, token);

        ResponseEntity<Void> response = webClient.post()
                .uri("/admin/realms/quickstart/users")
                .headers(h -> h.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRepresentation), UserRepresentation.class)
                .retrieve()
                .toBodilessEntity()
                .block();

        LOG.debug("Status: {}", response.getStatusCode());
    }

    public Integer getUserCount(String token) {
        LOG.info("Get User Count - token: {}", token);
        ResponseEntity<Integer> response = webClient.get()
                .uri("/admin/realms/quickstart/users/count")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .toEntity(Integer.class)
                .block();

        LOG.info("Status {}. Count: {}", response.getStatusCode(), response.getBody());
        return response.getBody();
    }
}
