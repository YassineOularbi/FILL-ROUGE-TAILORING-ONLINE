import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import { of } from 'rxjs';
import { Store } from '@ngrx/store';
import * as ProductActions from '../../product/actions/product.actions';
import { selectPage, selectPageSize, selectSortField, selectSortDirection } from '../../pagination/selectors/pagination.selectors';
import { ProductService } from '../../../services/product.service';
import { AppState } from '../../app.state';

@Injectable()
export class ProductEffects {

  private actions$ = inject(Actions);
  private productService = inject(ProductService);
  private store = inject(Store<AppState>);


  loadProducts$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProductActions.loadProducts),
      withLatestFrom(
        this.store.select(selectPage),
        this.store.select(selectPageSize),
        this.store.select(selectSortField),
        this.store.select(selectSortDirection)
      ),
      mergeMap(([action, page, size, sortField, sortDirection]) =>
        this.productService.getAllProducts(page, size, sortField, sortDirection).pipe(
          map(response => ProductActions.loadProductsSuccess({ 
            products: response.content, 
            totalRecords: response.totalElements 
          })),
          catchError(error => of(ProductActions.loadProductsFailure({ error: error.message })))
        )
      )
    )
  );

  searchProducts$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProductActions.searchProducts),
      mergeMap(({ query, page, size, sortField, sortDirection }) =>
        this.productService.search(query, page, size, sortField, sortDirection).pipe(
          map(response => ProductActions.loadProductsSuccess({
            products: response.content,
            totalRecords: response.totalElements
          })),
          catchError(error => of(ProductActions.loadProductsFailure({ error: error.message })))
        )
      )
    )
  );
}
