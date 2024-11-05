import { Component } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { FooterComponent } from "./_components/footer/footer.component";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    FooterComponent,
    RouterOutlet
],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
}
