import { Injectable } from '@angular/core';
import { KeycloakService as KeycloakAngularService, KeycloakOptions } from 'keycloak-angular';
import { KeycloakProfile, KeycloakLoginOptions, KeycloakLogoutOptions, KeycloakConfig } from 'keycloak-js';
import { environment } from '../../../environments/environment';
import { Observable, from, of } from 'rxjs';
import { map, catchError, retry } from 'rxjs/operators';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  constructor(
    private keycloakAngular: KeycloakAngularService, private http: HttpClient) { }

  init(): Promise<boolean> {
    const response = localStorage.getItem('keycloak');

    if (response) {
            const parsedResponse = JSON.parse(response);

      if (this.isAuthenticated()) {
        const initOptions: KeycloakOptions = {
          config: environment.keycloakConfig as KeycloakConfig,
          initOptions: {
            checkLoginIframe: false,
            onLoad: 'check-sso',
            silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`,
            token: parsedResponse.access_token,
            refreshToken: parsedResponse.refresh_token,
          },
        };
        return this.keycloakAngular.init(initOptions);
          } else {
            localStorage.removeItem('keycloak');
      }
    }

    const initOptions: KeycloakOptions = {
      config: environment.keycloakConfig as KeycloakConfig,
      initOptions: {
        checkLoginIframe: false,
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`,
        token: undefined,
        refreshToken: undefined,
      },
    };

    return this.keycloakAngular.init(initOptions);
  }

  signin(username: string, password: string): Observable<any> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password)
      .set('grant_type', 'password')
      .set('client_id', 'tailoring-online-id');

    return this.http.post('http://localhost:8080/realms/tailoring-online/protocol/openid-connect/token', body.toString(), {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
    });
  }

  login(options?: KeycloakLoginOptions): Promise<void> {
    return this.keycloakAngular.login(options);
  }

  logout(options?: KeycloakLogoutOptions): Promise<void> {
    localStorage.removeItem('keycloak');
    return this.keycloakAngular.logout(options?.redirectUri);
  }

  accountManagement(): Promise<void> {
    return this.keycloakAngular.getKeycloakInstance().accountManagement();
  }

  loadUserProfile(): Promise<KeycloakProfile> {
    return this.keycloakAngular.loadUserProfile();
  }

  isAuthenticated(): boolean {
    const isLoggedIn = this.keycloakAngular.isLoggedIn();

    if (!isLoggedIn) {
      localStorage.removeItem('keycloak');
      return false;
    }

    if (this.isTokenExpired()) {
      map(updated => {
        if (!updated) {
          localStorage.removeItem('keycloak');
          return false;
        }
        return true;
      })
    }

    return this.keycloakAngular.isLoggedIn();
  }

  isTokenExpired(): boolean {
    return this.keycloakAngular.isTokenExpired(5);
  }

  onTokenExpired(): Observable<boolean> {
    return this.updateToken(30).pipe(
      map(updated => {
        if (updated) {
          localStorage.removeItem('keycloak');
          const tokenResponse = {
            access_token: this.keycloakAngular.getToken(),
            expires_in: this.keycloakAngular.getKeycloakInstance()?.tokenParsed?.exp,
            refresh_expires_in: this.keycloakAngular.getKeycloakInstance()?.refreshTokenParsed?.exp,
            refresh_token: this.keycloakAngular.getKeycloakInstance()?.refreshToken,
            scope: "profile email",
            session_state: this.keycloakAngular.getKeycloakInstance()?.tokenParsed?.session_state,
            token_type: "Bearer",
          };
          localStorage.setItem('keycloak', JSON.stringify(tokenResponse));
        }
        return updated;
      }),
      catchError(() => of(false))
    );
  }

  updateToken(minValidity: number = 5): Observable<boolean> {
    return from(this.keycloakAngular.updateToken(minValidity)).pipe(
      retry({
        count: 3,
        delay: 1000
      }),
      catchError(() => of(false))
    );
  }

  getToken(): string | undefined {
    return this.keycloakAngular.getKeycloakInstance().token;
  }

  getRefreshToken(): string | undefined {
    return this.keycloakAngular.getKeycloakInstance().refreshToken;
  }

  getId(): string | undefined {
    return this.keycloakAngular.getKeycloakInstance().subject;
  }

  getTimeSkew(): Observable<number | undefined> {
    return of(this.keycloakAngular.getKeycloakInstance().timeSkew);
  }

  hasRealmRole(role: string): boolean {
    return this.keycloakAngular.isUserInRole(role);
  }

  hasResourceRole(role: string, resource?: string): boolean {
    return this.keycloakAngular.isUserInRole(role, resource);
  }

  getKeycloakInstance(): any {
    return this.keycloakAngular.getKeycloakInstance();
  }
}