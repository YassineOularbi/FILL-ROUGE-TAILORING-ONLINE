import { Routes } from '@angular/router';

export const CUSTOMIZATION_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./customization.component').then(m => m.CustomizationComponent),
    children: [
        {
            path:'measurements',
            loadComponent: () => import('./_components/measurements/measurements.component').then(m => m.MeasurementsComponent)
        },
        {
            path: 'options',
            loadComponent: () => import('./_components/options/options.component').then(m => m.OptionsComponent)
        }
    ]
  }
];
