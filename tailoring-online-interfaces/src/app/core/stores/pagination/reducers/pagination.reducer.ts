import { createReducer, on } from '@ngrx/store';
import { initialPaginationState } from '../pagination.state';
import * as PaginationActions from '../actions/pagination.actions';

export const paginationReducer = createReducer(
  initialPaginationState,
  on(PaginationActions.setPaginationDefaults, () => initialPaginationState),
  on(PaginationActions.setPage, (state, { page }) => ({ ...state, page })),
  on(PaginationActions.setPageSize, (state, { size }) => ({ ...state, size })),
  on(PaginationActions.setSort, (state, { sortField, sortDirection }) => ({
    ...state,
    sortField,
    sortDirection
  }))
);