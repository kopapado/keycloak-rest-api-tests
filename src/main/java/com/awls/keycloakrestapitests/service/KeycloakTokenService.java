package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.KeycloakConfiguration;
import com.awls.keycloakrestapitests.model.TokenDetails;
import com.awls.keycloakrestapitests.model.UserCredentials;
import com.awls.keycloakrestapitests.model.UserCredentialsBuilder;
import com.awls.keycloakrestapitests.utility.UserUtility;
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
        UserCredentials userCredentials = UserUtility.getAdminCredentials();
        return getToken(userCredentials);
    }

    public TokenDetails getSimpleUserToken() {
        UserCredentials userCredentials = getUserCredentials("alice", "alice");
        return getToken(userCredentials);
    }

    public TokenDetails getPremiumUserToken() {
        UserCredentials userCredentials = getUserCredentials("jdoe", "jdoe");
        return getToken(userCredentials);
    }

    private UserCredentials getUserCredentials(String username, String password) {
        return UserCredentialsBuilder.anUserCredentials()
                .withRealm("quickstart")
                .withClientId("authz-servlet")
                .withClientSecret("secret")
                .withUsername(username)
                .withPassword(password)
                .build();
    }
}
