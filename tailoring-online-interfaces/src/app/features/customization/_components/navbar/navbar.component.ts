import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  currentPath: string = '';
  showNextButton: boolean = false;
  showConfirmButton: boolean = false;
  budget: number = 23000; 

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.router.events.subscribe(() => {
      this.currentPath = this.router.url;
      console.log(this.currentPath);
      
      if (this.currentPath.includes('/customization/measurements')) {
        this.showNextButton = true;
        this.showConfirmButton = false;
      }
      else if (this.currentPath.includes('/customization/options')) {
        this.showNextButton = false;
        this.showConfirmButton = true;
      } else {
        this.showNextButton = false;
        this.showConfirmButton = false;
      }
    });
  }

  goToOptions(): void {
    this.router.navigate(['/customization/options']);
  }

  confirmCustomization(): void {
    console.log('Customization confirmed with budget:', this.budget);
  }
}
