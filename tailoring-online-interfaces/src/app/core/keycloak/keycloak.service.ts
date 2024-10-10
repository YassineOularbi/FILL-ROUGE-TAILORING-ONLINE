import { Injectable } from '@angular/core';
import Keycloak, { KeycloakInitOptions, KeycloakProfile, KeycloakLoginOptions, KeycloakLogoutOptions } from 'keycloak-js';
import { environment } from '../../../environments/environment';
import { catchError, from, Observable, of } from 'rxjs';
import { map, retry, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { KeycloakService as KeycloakAuthService } from 'keycloak-angular';


@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  keycloak?: Keycloak;

  constructor(private http: HttpClient, private keycloakService: KeycloakAuthService) {
  }

  init(): Promise<boolean | void> {
    this.keycloak = new Keycloak(environment.keycloakConfig);
    const initOptions: KeycloakInitOptions = {
      checkLoginIframe: false,
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: `${location.origin}/silent-check-sso.html`,
    };
    
    const response = localStorage.getItem('keycloak');
    if (response) {
      const parsedResponse = JSON.parse(response);
      initOptions.token = parsedResponse.access_token;
      initOptions.refreshToken = parsedResponse.refresh_token;
      initOptions.timeSkew
    }
    return this.keycloak.init(initOptions).catch(() => {
      window.location.href = '/internal-server-error';
  });
  }

  refreshToken(refresh_token: string): Observable<any> {
    const body = new HttpParams()
      .set('refresh_token', refresh_token)
      .set('grant_type', 'refresh_token')
      .set('client_id',  environment.keycloakConfig.clientId);

    return this.http.post(environment.keycloakUrl, body.toString(), {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
    });
  }

  login(options?: KeycloakLoginOptions): Promise<void> | undefined {
    return this.keycloak?.login(options);
  }

  logout(options?: KeycloakLogoutOptions): Promise<void> | undefined {
    localStorage.removeItem('keycloak');
    return this.keycloak?.logout(options);
  }

  accountManagement(): Promise<void> | undefined {
    return this.keycloak?.accountManagement();
  }

  loadUserProfile(): Promise<KeycloakProfile> | undefined {
    return this.keycloak?.loadUserProfile();
  }

  isLoggedIn(): boolean | undefined {
    console.log(this.keycloak?.authenticated);
    return this.keycloak?.authenticated;
  }

  isAuthenticated(): Observable<boolean> {
    if (!this.keycloak?.authenticated) {
      return of(false);
    }
    return this.isTokenExpired().pipe(
      tap(expired => {
        if (expired) {
          this.onTokenExpired();
        }
      }),
      map(expired => !expired && !!this.keycloak?.authenticated)
    );
  }


  isTokenExpired(): Observable<boolean | undefined> {
    return of(this.keycloak?.isTokenExpired(10));
  }

  onTokenExpired(): void {
    this.updateToken(10).subscribe({
      next: (updated) => {
        if (updated) {
          localStorage.removeItem('keycloak');
          const tokenResponse = {
            access_token: this.keycloak?.token,
            expires_in: this.keycloak?.tokenParsed?.exp,
            refresh_expires_in: this.keycloak?.refreshTokenParsed?.exp,
            refresh_token: this.keycloak?.refreshToken,
            scope: "profile email",
            session_state: this.keycloak?.tokenParsed?.session_state,
            token_type: "Bearer",
          };
          localStorage.setItem('keycloak', JSON.stringify(tokenResponse))
        }
      }
    });
  }

  updateToken(minValidity: number = 5): Observable<boolean> {
    if (!this.keycloak) {
      return of(false);
    }
    return from(this.keycloak.updateToken(minValidity)).pipe(
      retry({
        count: 3,
        delay: 1000
      }),
      catchError(() => of(false))
    );
  }

  getToken(): string | null | undefined {
    return this.keycloak?.authenticated ? this.keycloak?.token : null;
  }

  getRefreshToken(): string | null | undefined {
    return this.keycloak?.authenticated ? this.keycloak?.refreshToken : null;
  }

  getId(): string | undefined {
    return this.keycloak?.tokenParsed?.sub;
  }

  getTimeSkew() {
    return this.keycloak?.timeSkew;
  }

  hasRealmRole(role: string): boolean | undefined {
    return this.keycloak?.hasRealmRole(role);
  }

  hasResourceRole(role: string, resource?: string): boolean | undefined {
    return this.keycloak?.hasResourceRole(role, resource);
  }

  getKeycloakInstance(): Keycloak | undefined {
    return this.keycloak;
  }

  async onLoginWithProvider(provider: string): Promise<void> {
    await this.init();
    const loginOptions: KeycloakLoginOptions = {
      redirectUri: `${window.location.origin}/auth/signin?returnUrl=${encodeURIComponent(window.location.href)}`,
      idpHint: provider,
    };
    return this.login(loginOptions);
  }

}