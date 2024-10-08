import { Injectable } from '@angular/core';
import Keycloak, { KeycloakInitOptions, KeycloakProfile, KeycloakLoginOptions, KeycloakLogoutOptions } from 'keycloak-js';
import { environment } from '../../../environments/environment';
import {  Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  keycloak?: Keycloak;

  constructor(private http: HttpClient, private router: Router) {
  }

  init() {
    this.keycloak = new Keycloak(environment.keycloakConfig);
    const initOptions: KeycloakInitOptions = {
      checkLoginIframe: false,
    };
    this.keycloak.init(initOptions).then(
      () => {
        console.log(this.getKeycloakInstance()?.didInitialize);
        const response = localStorage.getItem('keycloak');
        const parsedResponse = JSON.parse(response!);
        this.keycloak = new Keycloak(environment.keycloakConfig);
        const initOptions: KeycloakInitOptions = {
          checkLoginIframe: false,
          token: parsedResponse.access_token,
          refreshToken: parsedResponse.refresh_token
        };
        this.keycloak.init(initOptions).then(()=>{
          console.log(this.keycloak);
          console.log(this.keycloak?.authenticated);
          console.log(this.keycloak?.isTokenExpired(500))
        });

        
        // if (this.getKeycloakInstance()?.didInitialize) {
        //   const authenticated = this.isAuthenticated();
        //   console.log(authenticated);
        // }


      });
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

  isAuthenticated(minValidity: number = 30): boolean | undefined {
    const response = localStorage.getItem('keycloak');
    if (response) {
      const parsedResponse = JSON.parse(response);
      if (this.keycloak?.isTokenExpired(minValidity)) {
        console.log(`Is token expired or close to expiration (minValidity: ${minValidity})? ${this.keycloak?.isTokenExpired(minValidity)}`);
        this.keycloak?.updateToken(minValidity).then(updated => {
          console.log(`Token update result: ${updated}`);
          if (updated) {
            const tokenParsed = this.keycloak?.tokenParsed;
            const refreshTokenParsed = this.keycloak?.refreshTokenParsed;

            console.log('Storing updated token in localStorage...');
            localStorage.removeItem('keycloak');

            const tokenResponse = {
              access_token: this.keycloak?.token,
              expires_in: tokenParsed?.exp,
              refresh_expires_in: refreshTokenParsed?.exp,
              refresh_token: this.keycloak?.refreshToken,
              scope: "profile email",
              session_state: tokenParsed?.session_state,
              token_type: "Bearer",
            };

            localStorage.setItem('keycloak', JSON.stringify(tokenResponse));
            console.log('Token successfully updated and stored in localStorage.');
            const response = localStorage.getItem('keycloak');
            if (response) {
              const parsedResponse = JSON.parse(response);
              this.keycloak = new Keycloak(environment.keycloakConfig);
              const initOptions: KeycloakInitOptions = {
                checkLoginIframe: false,
                onLoad: 'check-sso',
                silentCheckSsoRedirectUri: `${location.origin}/silent-check-sso.html`,
                token: parsedResponse.access_token,
                refreshToken: parsedResponse.refresh_token
              };
              this.keycloak.init(initOptions);
            }
          } else {
            console.log('failed to update token');
            
          }
        });
      } else {
        this.keycloak = new Keycloak(environment.keycloakConfig);
        const initOptions: KeycloakInitOptions = {
          checkLoginIframe: false,
          onLoad: 'check-sso',
          silentCheckSsoRedirectUri: `${location.origin}/silent-check-sso.html`,
          token: parsedResponse.access_token,
          refreshToken: parsedResponse.refresh_token
        };
        this.keycloak.init(initOptions);
      }
    }
    return (this.keycloak?.authenticated)
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
}