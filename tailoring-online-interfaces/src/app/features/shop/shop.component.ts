import { Component } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { HeroComponent } from "./_components/hero/hero.component";

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [NavbarComponent, HeroComponent],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.scss'
})
export class ShopComponent {

}
