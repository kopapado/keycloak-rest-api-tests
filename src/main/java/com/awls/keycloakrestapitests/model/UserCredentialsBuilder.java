package com.awls.keycloakrestapitests.model;

public final class UserCredentialsBuilder {
    private String realm;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;

    private UserCredentialsBuilder() {
    }

    public static UserCredentialsBuilder anUserCredentials() {
        return new UserCredentialsBuilder();
    }

    public UserCredentialsBuilder withRealm(String realm) {
        this.realm = realm;
        return this;
    }

    public UserCredentialsBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public UserCredentialsBuilder withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public UserCredentialsBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserCredentialsBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserCredentials build() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setRealm(realm);
        userCredentials.setClientId(clientId);
        userCredentials.setClientSecret(clientSecret);
        userCredentials.setUsername(username);
        userCredentials.setPassword(password);
        return userCredentials;
    }
}
