# Keycloak REST API Tests
This is a command line tool that interacts with Keycloak REST API and perform a number of tests programmatically.
The following actions are supported:
* user-creation
* token-retrieval
* client-access

Note that, as auth client, we used the Java integration client also used in the Keycloak PoC: https://github.com/kopapado/java-keycloak-integration

## Example commands
### User Creation
```
java -jar keycloak-rest-api-tests-0.0.1-SNAPSHOT.jar --test.action=user-creation --test.no-requests=100
```

### Token Retrieval
```
java -jar keycloak-rest-api-tests-0.0.1-SNAPSHOT.jar --test.action=token-retrieval --test.no-requests=100
```

### Client Access
```
java -jar keycloak-rest-api-tests-0.0.1-SNAPSHOT.jar --test.action=client-access --test.no-requests=100
```