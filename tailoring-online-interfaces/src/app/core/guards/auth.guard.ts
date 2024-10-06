import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { inject } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { KeycloakAuthService } from '../keycloak/keycloak-auth.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> => {
  const keycloakAuthService = inject(KeycloakAuthService);
  const router = inject(Router);
  
  return keycloakAuthService.isAuthenticated().pipe(
    map(isLoggedIn => {
      if (!isLoggedIn) {
        router.navigate(['/auth/signin'], { queryParams: { returnUrl: state.url } });
        return false;
      }
      return true;
    })
  );
}
