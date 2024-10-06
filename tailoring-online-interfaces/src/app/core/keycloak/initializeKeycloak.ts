import { KeycloakService } from 'keycloak-angular';

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  const isBrowser = typeof window !== 'undefined';

  const rememberMe = isBrowser && typeof localStorage !== 'undefined'
    ? localStorage.getItem('remember_me')
    : null;

  if (!isBrowser) {
    return () => Promise.resolve(true);
  }

  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080',
        realm: 'tailoring-online',
        clientId: 'tailoring-online-id',
      },
      initOptions: {
        onLoad: rememberMe ? 'check-sso' : 'login-required',
        checkLoginIframe: false,
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
    });
}
