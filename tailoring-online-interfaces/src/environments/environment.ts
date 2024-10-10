export const environment = {
    production: true,
    apiNotificationMailing: 'http://localhost:9191/NOTIFICATION-MAILING-SERVICE/',
    apiUserManagement: 'http://localhost:9191/USER-MANAGEMENT-SERVICE/',
    apiStoreManagement: 'http://localhost:9191/STORE-MANAGEMENT-SERVICE/',
    apiOrderManagement: 'http://localhost:9191/ORDER-MANAGEMENT-SERVICE/',
    apiPaymentBanking: 'http://localhost:9191/PAYMENT-BANKING-SERVICE/',
    apiLocalizationShipping: 'http://localhost:9191/LOCALIZATION-SHIPPING-SERVICE/',
    keycloakConfig: {
        url: 'http://localhost:8080',
        realm: 'tailoring-online',
        clientId: 'tailoring-online-id',
    },
    keycloakUrl: 'http://localhost:8080/realms/tailoring-online/protocol/openid-connect/token'
};
