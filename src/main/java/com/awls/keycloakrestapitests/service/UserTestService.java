package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.TestConfiguration;
import com.awls.keycloakrestapitests.model.TokenDetails;
import com.awls.keycloakrestapitests.model.UserCredentials;
import com.awls.keycloakrestapitests.model.UserRepresentation;
import com.awls.keycloakrestapitests.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserTestService {
    private final TestConfiguration testConfiguration;
    private final KeycloakTokenService keycloakTokenService;
    private final KeycloakUserService keycloakUserService;

    @Autowired
    public UserTestService(TestConfiguration testConfiguration, KeycloakTokenService keycloakTokenService, KeycloakUserService keycloakUserService) {
        this.testConfiguration = testConfiguration;
        this.keycloakTokenService = keycloakTokenService;
        this.keycloakUserService = keycloakUserService;
    }

    public void createUsers() {
        UserCredentials userCredentials = UserUtility.getAdminCredentials();;

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
}
