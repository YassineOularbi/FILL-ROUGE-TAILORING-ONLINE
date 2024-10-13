import { createReducer, on } from '@ngrx/store';
import * as PaginationActions from './pagination.actions';

export interface PaginationState {
  page: number;
  size: number;
  sortField: string;
  sortDirection: string;
  loading: boolean;
  error: any;
}

export const initialState: PaginationState = {
  page: 1,
  size: 10,
  sortField: 'name',
  sortDirection: 'asc',
  loading: false,
  error: null
};

export const paginationReducer = createReducer(
  initialState,
  on(PaginationActions.setPage, (state, { page }) => ({
    ...state,
    page
  })),
  on(PaginationActions.setPageSize, (state, { size }) => ({
    ...state,
    size
  })),
  on(PaginationActions.setSort, (state, { sortField, sortDirection }) => ({
    ...state,
    sortField,
    sortDirection
  })),
  on(PaginationActions.loadPagination, state => ({
    ...state,
    loading: true,
    error: null
  })),
  on(PaginationActions.loadPaginationSuccess, (state, { page, size, sortField, sortDirection }) => ({
    ...state,
    page,
    size,
    sortField,
    sortDirection,
    loading: false
  })),
  on(PaginationActions.loadPaginationFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);
