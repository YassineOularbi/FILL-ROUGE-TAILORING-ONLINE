import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivateChildFn } from '@angular/router';
import { Observable, of } from 'rxjs';
import { KeycloakService } from '../keycloak/keycloak.service';
import { Role } from '../enums/role.enum';

export const roleGuard: CanActivateChildFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
): Observable<boolean> => {
  const keycloakService = inject(KeycloakService);
  const router = inject(Router);
  
  const requiredRoles = route.data['roles'] as Role[] | undefined;

  if (requiredRoles) {
    const hasRequiredRole = requiredRoles.some(role => keycloakService.hasResourceRole(role));
    
    if (!hasRequiredRole) {
      router.navigate(['/internal-server-error']);
      return of(false);
    }
  }
  
  return of(true);
};
