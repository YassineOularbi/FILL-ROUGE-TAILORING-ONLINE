import { KeycloakService } from 'keycloak-angular';

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {


  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080',
        realm: 'tailoring-online',
        clientId: 'tailoring-online-id',
      },
      initOptions: {
        onLoad: 'check-sso',
        checkLoginIframe: false,
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
    });
}