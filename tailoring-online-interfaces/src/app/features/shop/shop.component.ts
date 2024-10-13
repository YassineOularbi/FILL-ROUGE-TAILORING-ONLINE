import { Component } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { HeroComponent } from "./_components/hero/hero.component";
import { FooterComponent } from './_components/footer/footer.component';
import { ProductComponent } from "../../shared/_components/product/product.component";

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [NavbarComponent, HeroComponent, FooterComponent, ProductComponent],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.scss'
})
export class ShopComponent {

}
