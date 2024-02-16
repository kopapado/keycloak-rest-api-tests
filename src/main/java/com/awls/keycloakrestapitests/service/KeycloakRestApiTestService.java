package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.TestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static java.lang.System.exit;

@Service
public class KeycloakRestApiTestService {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakRestApiTestService.class);

    private final UserTestService userTestService;
    private final TokenTestService tokenTestService;
    private final ClientTestService clientTestService;

    private final KeycloakTokenService keycloakTokenService;
    private final ClientService clientService;
    private final TestConfiguration testConfiguration;

    @Autowired
    public KeycloakRestApiTestService(
            UserTestService userTestService, TokenTestService tokenTestService, ClientTestService clientTestService,
            KeycloakTokenService keycloakTokenService, ClientService clientService, TestConfiguration testConfiguration) {
        this.userTestService = userTestService;
        this.tokenTestService = tokenTestService;
        this.clientTestService = clientTestService;
        this.keycloakTokenService = keycloakTokenService;
        this.clientService = clientService;
        this.testConfiguration = testConfiguration;
    }

    public void run() {
        keycloakTokenService.printKeycloakConfiguration();
        clientService.printClientConfiguration();
        LOG.info("Test configuration: {}", testConfiguration.toString());

        selectTestToRun(testConfiguration);
        exit(0);
    }

    private void selectTestToRun(TestConfiguration testConfiguration) {
        switch (testConfiguration.getName()) {
            case "user-creation":
                measure(userTestService::createUsers);
                break;
            case "token-retrieval":
                measure(tokenTestService::getTokens);
                break;
            case "admin-token-retrieval":
                measure(tokenTestService::getAdminTokens);
                break;
            case "simple-user-token-retrieval":
                measure(tokenTestService::getSimpleUserTokens);
                break;
            case "premium-user-token-retrieval":
                measure(tokenTestService::getPremiumUserTokens);
                break;
            case "client-access":
                measure(clientTestService::accessClient);
                break;
            case "client-premium-access":
                measure(clientTestService::accessClientPremium);
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
}
