package com.awls.keycloakrestapitests.service;

import com.awls.keycloakrestapitests.config.TestConfiguration;
import com.awls.keycloakrestapitests.model.TokenDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class ClientTestService {
    private final TestConfiguration testConfiguration;
    private final KeycloakTokenService keycloakTokenService;
    private final ClientService clientService;

    @Autowired
    public ClientTestService(TestConfiguration testConfiguration,
                             KeycloakTokenService keycloakTokenService, ClientService clientService) {
        this.testConfiguration = testConfiguration;
        this.keycloakTokenService = keycloakTokenService;
        this.clientService = clientService;
    }

    public void accessClientPremium() {
        accessClient(ClientService.CLIENT_PREMIUM_PATH, keycloakTokenService::getPremiumUserToken);
    }

    public void accessClient() {
        accessClient(ClientService.CLIENT_HOME_PATH, keycloakTokenService::getSimpleUserToken);
    }

    private void accessClient(String url, Supplier<TokenDetails> getToken) {
        TokenDetails tokenDetails = null;

        for (int i = 0; i < testConfiguration.getNoRequests(); i++) {
            if (i % testConfiguration.getRequestsPerSession() == 0) {
                tokenDetails = getToken.get();
            }

            clientService.access(url, tokenDetails.getAccess_token());
        }
    }
}
