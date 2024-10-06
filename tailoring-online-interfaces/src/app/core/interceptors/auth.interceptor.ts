import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakAuthService } from '../keycloak/keycloak-auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakAuthService);
  let authReq = req;
  const token = keycloakService.getAccessToken()
  if (token != null) {

    authReq = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + token) });

  }
  return next(authReq);
};
