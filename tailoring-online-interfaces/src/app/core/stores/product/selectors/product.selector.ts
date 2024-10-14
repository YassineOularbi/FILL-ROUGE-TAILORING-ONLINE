import { createSelector } from '@ngrx/store';
import { AppState } from '../../app.state';

export const selectProductState = (state: AppState) => state.product;

export const selectAllProducts = createSelector(
  selectProductState,
  (state) => state.products
);

export const selectProductLoading = createSelector(
  selectProductState,
  (state) => state.loading
);

export const selectProductError = createSelector(
  selectProductState,
  (state) => state.error
);

export const selectTotalRecords = createSelector(
  selectProductState,
  (state) => state.totalRecords
);
