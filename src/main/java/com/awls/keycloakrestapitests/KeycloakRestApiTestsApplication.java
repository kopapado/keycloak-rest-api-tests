package com.awls.keycloakrestapitests;

import com.awls.keycloakrestapitests.service.KeycloakRestApiTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeycloakRestApiTestsApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakRestApiTestsApplication.class);

    private final KeycloakRestApiTestService keycloakRestApiTestService;

    @Autowired
    public KeycloakRestApiTestsApplication(KeycloakRestApiTestService keycloakRestApiTestService) {
        this.keycloakRestApiTestService = keycloakRestApiTestService;
    }

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(KeycloakRestApiTestsApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");
        keycloakRestApiTestService.run();
    }
}
