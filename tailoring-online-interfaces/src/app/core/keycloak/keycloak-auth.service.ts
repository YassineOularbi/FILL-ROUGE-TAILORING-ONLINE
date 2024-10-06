import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable, from, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class KeycloakAuthService {

  constructor(private keycloakService: KeycloakService) { }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }

  login(rememberMe: boolean): Promise<void> {
    if (this.isBrowser() && rememberMe) {
      localStorage.setItem('rememberMe', 'true');
    } else if (this.isBrowser()) {
      localStorage.removeItem('rememberMe');
    }
    return this.keycloakService.login();
  }

  logout(): Promise<void> {
    if (this.isBrowser()) {
      localStorage.removeItem('rememberMe');
    }
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
    return of(isLoggedIn);
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
