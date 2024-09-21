import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-collections',
  standalone: true,
  imports: [
    MatCardModule
  ],
  templateUrl: './collections.component.html',
  styleUrl: './collections.component.scss'
})
export class CollectionsComponent {

}
