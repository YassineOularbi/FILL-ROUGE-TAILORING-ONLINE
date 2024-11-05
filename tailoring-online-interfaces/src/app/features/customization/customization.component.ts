import { Component} from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-customization',
  standalone: true,
  imports: [NavbarComponent, RouterOutlet],
  templateUrl: './customization.component.html',
  styleUrls: ['./customization.component.scss']
})
export class CustomizationComponent {
  
}
