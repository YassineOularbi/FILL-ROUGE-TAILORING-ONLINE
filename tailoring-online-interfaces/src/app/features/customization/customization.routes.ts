import { Routes } from '@angular/router';

export const CUSTOMIZATION_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./customization.component').then(m => m.CustomizationComponent),
  }
];
