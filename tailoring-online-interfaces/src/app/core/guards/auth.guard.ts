// import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
// import { inject } from '@angular/core';
// import { map, Observable, of } from 'rxjs';
// import { KeycloakService } from '../keycloak/keycloak.service';
// import { KeycloakLogoutOptions } from 'keycloak-js';

// export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> => {
//     const keycloakService = inject(KeycloakService);
//     const returnUrl = state.url;
//     const logoutOptions: KeycloakLogoutOptions = {
//         redirectUri: `${window.location.origin}/auth/signin?returnUrl=${encodeURIComponent(returnUrl)}`,
//         logoutMethod: 'GET',
//     };

//     if (!keycloakService.isAuthenticated()) {
//         keycloakService.logout(logoutOptions);
//         return of(false);
//     }

//     return of(true);
// }
