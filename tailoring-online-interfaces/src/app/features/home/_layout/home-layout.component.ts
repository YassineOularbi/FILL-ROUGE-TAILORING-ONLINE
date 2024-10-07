import { Component, HostListener } from '@angular/core';
import { MainComponent } from "../_components/main/main.component";
import { CollectionsComponent } from "../_components/collections/collections.component";
import { TailoringComponent } from "../_components/tailoring/tailoring.component";
import { ServicesComponent } from "../_components/services/services.component";
import { AboutUsComponent } from "../_components/about-us/about-us.component";
import { ContactUsComponent } from "../_components/contact-us/contact-us.component";
import { CommonModule, ViewportScroller } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-home-layout',
  standalone: true,
  imports: [MainComponent, CollectionsComponent, TailoringComponent, ServicesComponent, AboutUsComponent, ContactUsComponent, CommonModule, MatIconModule ],
  templateUrl: './home-layout.component.html',
  styleUrl: './home-layout.component.scss'
})
export class HomeLayoutComponent {
  showScrollTop = false;

  constructor(private viewportScroller: ViewportScroller) {}

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    const scrollPosition = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
    
    this.showScrollTop = scrollPosition > 400;
  }

  scrollToTop(): void {
    this.viewportScroller.scrollToPosition([0, 0]);
  }
}
