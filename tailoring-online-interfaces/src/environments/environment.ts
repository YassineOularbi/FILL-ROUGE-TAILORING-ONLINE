export const environment = {
    production: true,
    apiNotification: 'http://localhost:9191/NOTIFICATION-MAILING-SERVICE/',
    apiUser: 'http://localhost:9191/USER-MANAGEMENT-SERVICE/',
    keycloakConfig: {
        url: 'http://localhost:8080',
        realm: 'tailoring-online',
        clientId: 'tailoring-online-id',
    },
    keycloakUrl: 'http://localhost:8080/realms/tailoring-online/protocol/openid-connect/token'
};
