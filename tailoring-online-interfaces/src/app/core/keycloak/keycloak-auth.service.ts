import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable, of, from } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class KeycloakAuthService {

  constructor(private keycloakService: KeycloakService) { }

  login(): Promise<void> {
    return this.keycloakService.login();
  }

  logout(): Promise<void> {
    localStorage.removeItem('rememberMe');
    return this.keycloakService.logout();
  }

  forgotPassword(): Promise<void> {
    return this.keycloakService.login({
      action: 'RESET_CREDENTIALS',
    });
  }

  getAccessToken(): string | null {
    return this.keycloakService.getKeycloakInstance().token || null;
  }

  getRefreshToken(): string | null {
    return this.keycloakService.getKeycloakInstance().refreshToken || null;
  }

  updateToken(minValidity: number = 30): Observable<boolean> {
    return from(this.keycloakService.updateToken(minValidity));
  }

  isAuthenticated(): Observable<boolean> {
    const isLoggedIn = this.keycloakService.isLoggedIn();
    console.log(isLoggedIn);
    
    if (isLoggedIn) {
      return from(this.keycloakService.updateToken(30)).pipe(
        map(() => true),
        catchError(() => {
          console.error('Error refreshing token');
          return of(false);
        })
      );
    } else {
      return of(false);
    }
  }

  loadUserProfile(): Promise<Keycloak.KeycloakProfile> {
    return this.keycloakService.loadUserProfile();
  }

  loginWithIdentityProvider(provider: string): void {
    this.keycloakService.login({
      idpHint: provider,
    }).catch(error => {
      console.error(`Error logging in with ${provider}`, error);
    });
  }
}
