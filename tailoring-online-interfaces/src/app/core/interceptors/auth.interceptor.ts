import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakAuthService } from '../keycloak/keycloak-auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // const keycloakService = inject(KeycloakAuthService);
  // const token = keycloakService.getToken()

  // if (token) {
  //   const clonedReq = req.clone({
  //     setHeaders: {
  //       Authorization: `Bearer ${token}`
  //     }
  //   });
  //   return next(clonedReq);
  // }
  return next(req);
};
