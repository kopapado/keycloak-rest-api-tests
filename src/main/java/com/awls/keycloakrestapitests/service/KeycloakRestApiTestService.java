package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.model.TokenDetails;
import com.awls.keycloakrestapitests.model.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class KeycloakRestApiTestService {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakRestApiTestService.class);
    private static final Long NO_USERS = 1000000L;
    private static final Long USERS_PER_SESSION = 10000L;
    private static final Long NO_SESSIONS = NO_USERS / USERS_PER_SESSION;

    private final KeycloakTokenService keycloakTokenService;
    private final KeycloakUserService keycloakUserService;

    @Autowired
    public KeycloakRestApiTestService(KeycloakTokenService keycloakTokenService, KeycloakUserService keycloakUserService) {
        this.keycloakTokenService = keycloakTokenService;
        this.keycloakUserService = keycloakUserService;
    }

    public void run() {
        keycloakTokenService.printKeycloakConfiguration();

        measureUserCreationTime();

        TokenDetails adminToken = keycloakTokenService.getAdminToken();
        keycloakUserService.getUserCount(adminToken.getAccess_token());
    }

    private void createUsers() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);

        TokenDetails adminToken = keycloakTokenService.getAdminToken();
        for (int j = 0; j < NO_SESSIONS; j++) {
            for (int i = 0; i < USERS_PER_SESSION; i++) {
                userRepresentation.setUsername(UUID.randomUUID().toString());
                keycloakUserService.createUser(userRepresentation, adminToken.getAccess_token());
            }

            adminToken = keycloakTokenService.getAdminToken();
        }
    }

    private void measureUserCreationTime() {
        Instant start = Instant.now();
        createUsers();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        LOG.info("Time elapsed: {} ms", timeElapsed);
        LOG.info("User creation time: {} ms", timeElapsed / NO_USERS);
    }
}
