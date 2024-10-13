import { createSelector } from '@ngrx/store';
import { AppState } from '../../app.state';

export const selectPaginationState = (state: AppState) => state.pagination;

export const selectPage = createSelector(
  selectPaginationState,
  (state) => state.page
);

export const selectPageSize = createSelector(
  selectPaginationState,
  (state) => state.size
);

export const selectSortField = createSelector(
  selectPaginationState,
  (state) => state.sortField
);

export const selectSortDirection = createSelector(
  selectPaginationState,
  (state) => state.sortDirection
);