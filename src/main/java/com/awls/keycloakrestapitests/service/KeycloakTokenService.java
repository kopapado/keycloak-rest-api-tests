package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.KeycloakConfiguration;
import com.awls.keycloakrestapitests.model.TokenDetails;
import com.awls.keycloakrestapitests.model.UserCredentials;
import com.awls.keycloakrestapitests.model.UserCredentialsBuilder;
import io.netty.handler.timeout.WriteTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

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

    public TokenDetails getToken(UserCredentials userCredentials) {
        LOG.debug("Get Token - User Credentials: {}", userCredentials);
        BodyInserters.FormInserter<String> formInserter = BodyInserters
                .fromFormData("client_id", userCredentials.getClientId())
                .with("username", userCredentials.getUsername())
                .with("password", userCredentials.getPassword())
                .with("grant_type", "password");
        Optional.ofNullable(userCredentials.getClientSecret()).ifPresent(
                secret -> formInserter.with("client_secret", secret));

        ResponseEntity<TokenDetails> response = webClient.post()
                .uri("/realms/" + userCredentials.getRealm() + "/protocol/openid-connect/token")
                .body(formInserter)
                .retrieve()
                .toEntity(TokenDetails.class)
                .doOnError(WriteTimeoutException.class, ex -> LOG.error("Get Token Timeout"))
                .block();

        LOG.debug("Status: {}, Token Details: {}", response.getStatusCode(), response.getBody());
        return response.getBody();

    }

    public TokenDetails getAdminToken() {
        UserCredentials userCredentials = UserCredentialsBuilder.anUserCredentials()
                .withRealm("master")
                .withClientId("admin-cli")
                .withUsername("admin")
                .withPassword("admin")
                .build();

        return getToken(userCredentials);
    }

    public TokenDetails getSimpleUserToken() {
        UserCredentials userCredentials = UserCredentialsBuilder.anUserCredentials()
                .withRealm("quickstart")
                .withClientId("authz-servlet")
                .withClientSecret("secret")
                .withUsername("alice")
                .withPassword("alice")
                .build();

        return getToken(userCredentials);
    }

    public TokenDetails getPremiumUserToken() {
        UserCredentials userCredentials = UserCredentialsBuilder.anUserCredentials()
                .withRealm("quickstart")
                .withClientId("authz-servlet")
                .withClientSecret("secret")
                .withUsername("jdoe")
                .withPassword("jdoe")
                .build();

        return getToken(userCredentials);
    }
}
