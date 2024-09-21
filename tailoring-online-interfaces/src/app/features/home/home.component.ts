import { Component, HostListener } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { MainComponent } from './_components/main/main.component';
import { CollectionsComponent } from './_components/collections/collections.component';
import { CommonModule, ViewportScroller } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { TailoringComponent } from './_components/tailoring/tailoring.component';
import { ServicesComponent } from "./_components/services/services.component";
import { AboutUsComponent } from "./_components/about-us/about-us.component";
import { ContactUsComponent } from "./_components/contact-us/contact-us.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    MainComponent,
    CollectionsComponent,
    CommonModule,
    MatIconModule,
    TailoringComponent,
    ServicesComponent,
    AboutUsComponent,
    ContactUsComponent
],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
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
