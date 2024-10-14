import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';
import { roleGuard } from '../../core/guards/role.guard';
import { Role } from '../../core/enums/role.enum';

export const CUSTOMIZATION_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./customization.component').then(m => m.CustomizationComponent),
    canActivate: [authGuard],
    canActivateChild: [roleGuard],
    data: { roles: [Role.ADMIN, Role.CUSTOMER] },
    children: [
        {
            path: '',
            redirectTo: 'measurements',
            pathMatch: 'full'
        },
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
