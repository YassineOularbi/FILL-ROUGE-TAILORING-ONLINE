// import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
// import { inject } from '@angular/core';
// import { Observable, of } from 'rxjs';
// import { KeycloakAuthService } from '../keycloak/keycloak-auth.service';

// export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> => {
//   const keycloakAuthService = inject(KeycloakAuthService);
  
//   if (keycloakAuthService.isLoggedIn()) {    
//     return of(true);
//   } else {
//     keycloakAuthService.login();
//     return of(false);
//   }
// }
