import { ViewportScroller } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {

  constructor(private viewportScroller: ViewportScroller){}

  scrollToSection(section: string): void {
    this.viewportScroller.scrollToAnchor(section);
  }

}
