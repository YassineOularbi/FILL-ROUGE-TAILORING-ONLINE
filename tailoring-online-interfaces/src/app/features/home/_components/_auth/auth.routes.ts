import { Routes } from '@angular/router';

export const AUTH_ROUTES: Routes = [
    {
        path: 'signin',
        loadComponent: () => import('./signin/signin.component').then(m => m.SigninComponent),
    },
    {
        path: 'forgot-password',
        loadComponent: () => import('./forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent)
    },
    {
        path: 'signup',
        loadChildren: () => import('./signup/signup.routes').then(m => m.SIGNUP_ROUTES),
    }
];