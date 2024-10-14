import { createAction, props } from '@ngrx/store';
import { Product } from '../../../interfaces/product.interface';

export const loadProducts = createAction(
  '[Product] Load Products',
  props<{ page: number, size: number, sortField: string, sortDirection: string }>()
);

export const searchProducts = createAction(
  '[Product] Search Products',
  props<{ query: string, page: number, size: number, sortField: string, sortDirection: string }>()
);

export const loadProductsSuccess = createAction(
  '[Product] Load Products Success',
  props<{ products: Product[], totalRecords: number }>()
);

export const loadProductsFailure = createAction(
  '[Product] Load Products Failure',
  props<{ error: string }>()
);
