import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { ViewportScroller } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { KeycloakAuthService } from '../../../../core/keycloak/keycloak-auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

  constructor(private viewportScroller: ViewportScroller, private router: Router, private keycloakAuthService: KeycloakAuthService){}

  scrollToSection(section: string): void {
    if (this.router.url === '/') {
      this.viewportScroller.scrollToAnchor(section);
    } else {
      this.router.navigate(['/']).then(() => {
        setTimeout(() => {
          this.viewportScroller.scrollToAnchor(section);
        }, 500)
      });
    }
  }

  onLogin(){
    this.keycloakAuthService.login();
  }

}
