import { Component } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { MainComponent } from './_components/main/main.component';
import { CollectionsComponent } from './_components/collections/collections.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    MainComponent,
    CollectionsComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
