package com.awls.keycloakrestapitests.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "test")
public class TestConfiguration {
    private String action;
    private Long noRequests;
    private Long requestsPerSession;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getNoRequests() {
        return noRequests;
    }

    public void setNoRequests(Long noRequests) {
        this.noRequests = noRequests;
    }

    public Long getRequestsPerSession() {
        return requestsPerSession;
    }

    public void setRequestsPerSession(Long requestsPerSession) {
        this.requestsPerSession = requestsPerSession;
    }

    @Override
    public String toString() {
        return "TestConfiguration{" +
                "action='" + action + '\'' +
                ", noRequests=" + noRequests +
                ", requestsPerSession=" + requestsPerSession +
                '}';
    }
}
