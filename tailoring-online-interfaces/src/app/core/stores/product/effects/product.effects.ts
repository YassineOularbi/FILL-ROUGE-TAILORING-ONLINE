import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import { of } from 'rxjs';
import { Store } from '@ngrx/store';
import * as ProductActions from '../../product/actions/product.actions';
import * as PaginationActions from '../../pagination/actions/pagination.actions';
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
          map(response => {
            this.store.dispatch(ProductActions.loadProductsSuccess({ 
              products: response.content, 
              totalRecords: response.totalElements 
            }));

            this.store.dispatch(PaginationActions.setTotalRecords({ totalRecords: response.totalElements }));

            return response;
          }),
          catchError(error => of(ProductActions.loadProductsFailure({ error: error.message })))
        )
      )
    )
  );
}
