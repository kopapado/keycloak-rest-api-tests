package com.awls.keycloakrestapitests.utility;

import com.awls.keycloakrestapitests.model.UserCredentials;
import com.awls.keycloakrestapitests.model.UserCredentialsBuilder;

public final class UserUtility {
    public static UserCredentials getAdminCredentials() {
        return UserCredentialsBuilder.anUserCredentials()
                .withRealm("master")
                .withClientId("admin-cli")
                .withUsername("admin")
                .withPassword("admin")
                .build();
    }
}
