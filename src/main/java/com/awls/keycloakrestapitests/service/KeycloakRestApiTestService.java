package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.TestConfiguration;
import com.awls.keycloakrestapitests.model.TokenDetails;
import com.awls.keycloakrestapitests.model.UserCredentials;
import com.awls.keycloakrestapitests.model.UserCredentialsBuilder;
import com.awls.keycloakrestapitests.model.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static java.lang.System.exit;

@Service
public class KeycloakRestApiTestService {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakRestApiTestService.class);

    private final KeycloakTokenService keycloakTokenService;
    private final KeycloakUserService keycloakUserService;
    private final TestConfiguration testConfiguration;

    @Autowired
    public KeycloakRestApiTestService(
            KeycloakTokenService keycloakTokenService, KeycloakUserService keycloakUserService, TestConfiguration testConfiguration) {
        this.keycloakTokenService = keycloakTokenService;
        this.keycloakUserService = keycloakUserService;
        this.testConfiguration = testConfiguration;
    }

    public void run() {
        keycloakTokenService.printKeycloakConfiguration();
        LOG.info("Test configuration: {}", testConfiguration.toString());

        selectTestToRun(testConfiguration);
        exit(0);
    }

    private void selectTestToRun(TestConfiguration testConfiguration) {
        switch (testConfiguration.getName()) {
            case "user-creation":
                measure(this::createUsers);
                break;
            case "token-retrieval":
                measure(this::getTokens);
                break;
            case "admin-token-retrieval":
                measure(this::getAdminTokens);
                break;
            case "simple-user-token-retrieval":
                measure(this::getSimpleUserTokens);
                break;
            case "premium-user-token-retrieval":
                measure(this::getPremiumUserTokens);
                break;
            default:
                LOG.error("Wrong test name - Test configuration: {}", testConfiguration);
        }
    }

    private void measure(Runnable testRunner) {
        LOG.info("Test {} started", testConfiguration.getName());
        Instant start = Instant.now();
        testRunner.run();
        Instant finish = Instant.now();
        LOG.info("Test {} finished", testConfiguration.getName());

        long timeElapsed = Duration.between(start, finish).toMillis();
        LOG.info("Time elapsed: {} ms", timeElapsed);
        BigDecimal timePerRequest = BigDecimal.valueOf(timeElapsed).divide(
                BigDecimal.valueOf(testConfiguration.getNoRequests()));
        LOG.info("Time per request: {} ms", timePerRequest);
    }

    private void createUsers() {
        UserCredentials userCredentials = getAdminCredentials();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);

        TokenDetails adminToken = null;
        for (int j = 0; j < testConfiguration.getNoRequests(); j++) {
            if (j % testConfiguration.getRequestsPerSession() == 0) {
                adminToken = keycloakTokenService.getToken(userCredentials);
            }

            userRepresentation.setUsername(UUID.randomUUID().toString());
            keycloakUserService.createUser(userRepresentation, adminToken.getAccess_token());
        }
    }

    private void getTokens() {
        UserCredentials userCredentials = getAdminCredentials();

        for (int i = 0; i < testConfiguration.getNoRequests(); i++) {
            keycloakTokenService.getToken(userCredentials);
        }
    }

    private void getAdminTokens() {
        getTokens(keycloakTokenService::getAdminToken);
    }

    private void getSimpleUserTokens() {
        getTokens(keycloakTokenService::getSimpleUserToken);
    }

    private void getPremiumUserTokens() {
        getTokens(keycloakTokenService::getPremiumUserToken);
    }

    private void getTokens(Runnable runnable) {
        for (int i = 0; i < testConfiguration.getNoRequests(); i++) {
            runnable.run();
        }
    }

    private UserCredentials getAdminCredentials() {
        return UserCredentialsBuilder.anUserCredentials()
                .withRealm("master")
                .withClientId("admin-cli")
                .withUsername("admin")
                .withPassword("admin")
                .build();
    }
}
