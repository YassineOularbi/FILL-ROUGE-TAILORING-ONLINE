import { Routes } from '@angular/router';
import { authGuard } from '../../../../core/guards/auth.guard';

export const AUTH_ROUTES: Routes = [
    {
        path: 'signin',
        loadComponent: () => import('./signin/signin.component').then(m => m.SigninComponent),
    },
    {
        path: 'signup',
        loadComponent: () => import('./signup/signup.component').then(m => m.SignupComponent),
        canActivate: [authGuard]
    }
];