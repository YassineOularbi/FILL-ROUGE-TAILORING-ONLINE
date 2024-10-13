import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { HeroComponent } from "./_components/hero/hero.component";
import { FooterComponent } from './_components/footer/footer.component';
import { ProductComponent } from "../../shared/_components/product/product.component";
import { PaginationComponent } from "../../shared/_components/pagination/pagination.component";
import { KeycloakService } from '../../core/keycloak/keycloak.service';

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [NavbarComponent, HeroComponent, FooterComponent, ProductComponent, PaginationComponent],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.scss'
})
export class ShopComponent implements OnInit {

  constructor(private keycloakService: KeycloakService) {}

  ngOnInit(): void {
    console.log(this.keycloakService.hasResourceRole("ADMIN"));
  }

}
