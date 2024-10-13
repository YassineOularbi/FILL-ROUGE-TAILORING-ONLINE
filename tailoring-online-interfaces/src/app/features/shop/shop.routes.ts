import { Routes } from '@angular/router';
import { provideState } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { paginationReducer } from '../../core/stores/pagination/reducers/pagination.reducer';
import { PaginationEffects } from '../../core/stores/pagination/effects/pagination.effects';
import { roleGuard } from '../../core/guards/role.guard';
import { Role } from '../../core/enums/role.enum';

export const SHOP_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () => import('./shop.component').then(m => m.ShopComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.CUSTOMER, Role.ADMIN]},
        providers: [
            provideState('pagination', paginationReducer),
            provideEffects(PaginationEffects)
        ]
    }
];