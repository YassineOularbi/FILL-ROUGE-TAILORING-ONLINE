import { Component} from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';

@Component({
  selector: 'app-customization',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './customization.component.html',
  styleUrls: ['./customization.component.scss']
})
export class CustomizationComponent {
  
}
