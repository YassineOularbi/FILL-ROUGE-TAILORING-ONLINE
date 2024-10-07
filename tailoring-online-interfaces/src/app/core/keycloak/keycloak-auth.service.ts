import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import Keycloak from 'keycloak-js';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class KeycloakAuthService {

  keycloak?: Keycloak;

  init() {
    this.keycloak = new Keycloak(environment.keycloakConfig);
    this.keycloak.init({
      checkLoginIframe: false
    });
  }

  constructor(private router: Router) {}

  login() {
    return this.keycloak?.login();
  }

  isLoggedIn() {
    if (this.isTokenExpired()) {
      try {
        this.keycloak?.updateToken();
      } catch (error) {
        return null;
      }
    }
    console.log("authenticated", this.keycloak?.authenticated);
    return this.keycloak?.authenticated;
  }

  isTokenExpired() {
    console.log("expired", this.keycloak?.isTokenExpired());
    return this.keycloak?.isTokenExpired();
  }

  logout() {
    this.keycloak?.logout({
      redirectUri: location.origin
    });
    this.router.navigate(['/'])
  }

  getToken() {
    if (!this.isLoggedIn()) {
      return null;
    }
    console.log("token :", this.keycloak?.token);
    return this.keycloak?.token;
  }
}
