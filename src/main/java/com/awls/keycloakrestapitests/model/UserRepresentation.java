package com.awls.keycloakrestapitests.model;

public class UserRepresentation {
    private String username;
    private boolean enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserRepresentation{" +
                "username='" + username + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
