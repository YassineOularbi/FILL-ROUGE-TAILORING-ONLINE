import { createFeatureSelector, createSelector } from '@ngrx/store';
import { PaginationState } from './pagination.reducer';

export const selectPaginationState = createFeatureSelector<PaginationState>('pagination');

export const selectPage = createSelector(
  selectPaginationState,
  (state: PaginationState) => state.page
);

export const selectPageSize = createSelector(
  selectPaginationState,
  (state: PaginationState) => state.size
);

export const selectSortField = createSelector(
  selectPaginationState,
  (state: PaginationState) => state.sortField
);

export const selectSortDirection = createSelector(
  selectPaginationState,
  (state: PaginationState) => state.sortDirection
);

export const selectLoading = createSelector(
  selectPaginationState,
  (state: PaginationState) => state.loading
);

export const selectError = createSelector(
  selectPaginationState,
  (state: PaginationState) => state.error
);
