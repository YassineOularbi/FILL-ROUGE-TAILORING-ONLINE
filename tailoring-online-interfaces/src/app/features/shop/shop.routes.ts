import { Routes } from '@angular/router';
import { provideStore } from '@ngrx/store';
import { paginationReducer } from '../../core/stores/pagination/pagination.reducer';
import { provideEffects } from '@ngrx/effects';
import { PaginationEffects } from '../../core/stores/pagination/pagination.effects';

export const SHOP_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./shop.component').then(m => m.ShopComponent),
    providers: [
        provideStore({ pagination: paginationReducer }),
        provideEffects([PaginationEffects])
      ]
  }
];
