import { Component, OnInit } from '@angular/core';
import { KeycloakService } from '../../../../../core/keycloak/keycloak.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent implements OnInit {

  constructor(private keycloakService: KeycloakService) {}

  ngOnInit(): void {
    console.log(this.keycloakService.getToken());
    console.log(this.keycloakService.getRefreshToken());
    console.log(this.keycloakService.isTokenExpired());
    console.log(this.keycloakService.getTimeSkew());
    console.log(this.keycloakService.getKeycloakInstance());
    console.log(this.keycloakService.isAuthenticated());
  }

}
