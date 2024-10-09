import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadChildren: () => import('./features/home/home.routes').then(m => m.HOME_ROUTES),
    },
    {
        path: 'internal-server-error',
        loadComponent: () => import('./shared/pages/internal-server-error/internal-server-error.component').then(m => m.InternalServerErrorComponent),
    },
    {
        path: '**',
        loadComponent: () => import('./shared/pages/page-not-found/page-not-found.component').then(m => m.PageNotFoundComponent),
    }
];
