import { Routes } from '@angular/router';
import { provideState } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { paginationReducer } from '../../core/stores/pagination/reducers/pagination.reducer';
import { PaginationEffects } from '../../core/stores/pagination/effects/pagination.effects';

export const SHOP_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () => import('./shop.component').then(m => m.ShopComponent),
        providers: [
            provideState('pagination', paginationReducer),
            provideEffects(PaginationEffects)
        ]
    }
];