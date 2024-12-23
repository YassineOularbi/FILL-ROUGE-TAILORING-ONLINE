import { ViewportScroller } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {
  
  constructor(private viewportScroller: ViewportScroller){}

  scrollToSection(section: string): void {
    this.viewportScroller.scrollToAnchor(section);
  }
}
