import { Component, OnInit, Renderer2 } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  constructor(private renderer: Renderer2) {}

  ngOnInit(): void {
    // this.applyThemeBasedOnTime();
  }

  applyThemeBasedOnTime(): void {
    const currentHour = new Date().getHours();
    const nightStart = 19; 
    const nightEnd = 7; 

    if (currentHour >= nightStart || currentHour < nightEnd) {
      this.renderer.addClass(document.documentElement, 'dark');
    } else {
      this.renderer.removeClass(document.documentElement, 'dark');
    }
  }
}
