import { Component } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { MainComponent } from './_components/main/main.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    MainComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
