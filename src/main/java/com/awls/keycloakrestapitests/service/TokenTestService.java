package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.TestConfiguration;
import com.awls.keycloakrestapitests.model.UserCredentials;
import com.awls.keycloakrestapitests.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenTestService {
    private final TestConfiguration testConfiguration;
    private final KeycloakTokenService keycloakTokenService;

    @Autowired
    public TokenTestService(TestConfiguration testConfiguration, KeycloakTokenService keycloakTokenService) {
        this.testConfiguration = testConfiguration;
        this.keycloakTokenService = keycloakTokenService;
    }

    public void getTokens() {
        UserCredentials userCredentials = UserUtility.getAdminCredentials();

        for (int i = 0; i < testConfiguration.getNoRequests(); i++) {
            keycloakTokenService.getToken(userCredentials);
        }
    }

    public void getAdminTokens() {
        getTokens(keycloakTokenService::getAdminToken);
    }

    public void getSimpleUserTokens() {
        getTokens(keycloakTokenService::getSimpleUserToken);
    }

    public void getPremiumUserTokens() {
        getTokens(keycloakTokenService::getPremiumUserToken);
    }

    private void getTokens(Runnable runnable) {
        for (int i = 0; i < testConfiguration.getNoRequests(); i++) {
            runnable.run();
        }
    }
}
