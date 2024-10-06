import { KeycloakService } from 'keycloak-angular';

export function initializeKeycloakDefault(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080',
        realm: 'tailoring-online',
        clientId: 'tailoring-online-id',
      },
      initOptions: {
        checkLoginIframe: false
      },
    });
}

export function initializeKeycloakWithOptions(keycloak: KeycloakService): () => Promise<boolean> {
  return () => {
    const rememberMe = localStorage.getItem('rememberMe') === 'true';

    return keycloak.init({
      config: {
        url: 'http://localhost:8080',
        realm: 'tailoring-online',
        clientId: 'tailoring-online-id',
      },
      initOptions: {
        onLoad: rememberMe ? 'check-sso' : 'login-required',
        checkLoginIframe: false
      },
    });
  };
}
