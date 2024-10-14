import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        loadChildren: () => import('./features/home/home.routes').then(m => m.HOME_ROUTES),
    },
    {
        path: 'shop',
        loadChildren: () => import('./features/shop/shop.routes').then(m => m.SHOP_ROUTES),
        canActivate: [authGuard]
    },
    {
        path: 'customization',
        loadChildren: () => import('./features/customization/customization.routes').then(m => m.CUSTOMIZATION_ROUTES),
        canActivate: [authGuard]
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