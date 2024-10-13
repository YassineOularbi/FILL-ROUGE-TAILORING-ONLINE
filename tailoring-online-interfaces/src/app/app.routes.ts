import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadChildren: () => import('./features/home/home.routes').then(m => m.HOME_ROUTES),
    },
    {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.DASHBOARD_ROUTES)
    },
    {
        path: 'shop',
        loadChildren: () => import('./features/shop/shop.routes').then(m => m.SHOP_ROUTES)
    },
    {
        path: 'customization',
        loadChildren: () => import('./features/customization/customization.routes').then(m => m.CUSTOMIZATION_ROUTES)
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