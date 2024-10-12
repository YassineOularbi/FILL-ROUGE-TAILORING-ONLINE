import { Component } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { HeroComponent } from "./_components/hero/hero.component";
import { FooterComponent } from './_components/footer/footer.component';

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [NavbarComponent, HeroComponent, FooterComponent],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.scss'
})
export class ShopComponent {

}
