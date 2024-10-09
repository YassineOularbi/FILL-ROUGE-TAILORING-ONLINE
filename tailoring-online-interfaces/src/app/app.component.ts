import { Component, OnInit, OnDestroy } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, RouterOutlet } from '@angular/router';
import { LoadingComponent } from "./shared/animations/loading/loading.component";
import { CommonModule } from '@angular/common';
import { KeycloakService } from './core/keycloak/keycloak.service';
import { jwtDecode } from 'jwt-decode';
import { KeycloakLogoutOptions } from 'keycloak-js';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoadingComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  isLoading = false;
  private intervalId: any;
  private checkInterval = 1000;

  constructor(private router: Router, private keycloakService: KeycloakService) { }

  ngOnInit(): void {    
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.isLoading = true;
      } else if (
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError
      ) {
        setTimeout(() => {
          this.isLoading = false;
        }, 2000);
      }
    });

    this.startTokenCheck();
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  startTokenCheck(): void {    
    this.intervalId = setInterval(() => {
      const response = localStorage.getItem('keycloak');
      if (response) {
        const parsedResponse = JSON.parse(response);
        const decodedAccessToken: any = jwtDecode(parsedResponse.access_token);
        const decodedRefreshToken: any = jwtDecode(parsedResponse.refresh_token);
        const currentTime = Math.floor(Date.now() / 1000);
        if (currentTime >= decodedAccessToken.exp) {
          this.keycloakService.refreshToken(parsedResponse.refresh_token).subscribe({
            next: (response) => {
              localStorage.setItem('keycloak', JSON.stringify(response));
            },
            error: (err) => {
              this.logoutUser()
            }
          });
        }

        if (currentTime >= decodedRefreshToken.exp) {
          this.logoutUser()
        }
      }
    }, this.checkInterval);
  }

  logoutUser(): void {
    const logoutOptions: KeycloakLogoutOptions = {
      redirectUri: `${window.location.origin}/auth/signin?returnUrl=${encodeURIComponent(window.location.href)}`,
      logoutMethod: 'GET'
    };
    this.keycloakService.logout(logoutOptions);
}

}
