import { Component } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.scss'
})
export class ShopComponent {

}
