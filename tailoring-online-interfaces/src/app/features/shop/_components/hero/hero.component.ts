import { Component } from '@angular/core';
import { CarouselModule } from 'primeng/carousel';


@Component({
  selector: 'app-hero',
  standalone: true,
  imports: [
    CarouselModule
  ],
  templateUrl: './hero.component.html',
  styleUrl: './hero.component.scss'
})
export class HeroComponent {
  images: string[];
  responsiveOptions: any;

  constructor() {
    this.images = [
      '/assets/shop-bg-1.png',
      '/assets/shop-bg-2.png',
      '/assets/shop-bg-3.png',
      '/assets/shop-bg-4.png',
      '/assets/shop-bg-5.png',
      '/assets/shop-bg-6.png',
    ];

    this.responsiveOptions = [
      {
        breakpoint: '1024px',
        numVisible: 1,
        numScroll: 1,
      },
      {
        breakpoint: '768px',
        numVisible: 1,
        numScroll: 1,
      },
      {
        breakpoint: '560px',
        numVisible: 1,
        numScroll: 1,
      },
    ];
  }

}
