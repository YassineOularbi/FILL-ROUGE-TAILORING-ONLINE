import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { catchError, map, Observable, of, from } from 'rxjs';
import { KeycloakService } from '../keycloak/keycloak.service';
import { KeycloakLogoutOptions } from 'keycloak-js';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> => {
    const keycloakService = inject(KeycloakService);
    const logoutOptions: KeycloakLogoutOptions = {
        redirectUri: `${window.location.origin}/auth/signin?returnUrl=${encodeURIComponent(window.location.href)}`,
        logoutMethod: 'GET',
    };

    return from(keycloakService.init()).pipe(
        map(() => {
            if (!keycloakService.isLoggedIn()) {
                keycloakService.logout(logoutOptions);
                return false;
            }
            return true;
        }),
        catchError(() => {
            keycloakService.logout(logoutOptions);
            return of(false);
        })
    );
};
