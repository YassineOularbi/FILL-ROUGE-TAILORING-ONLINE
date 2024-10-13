import { createAction, props } from '@ngrx/store';

export const setPage = createAction(
  '[Pagination] Set Page',
  props<{ page: number }>()
);

export const setPageSize = createAction(
  '[Pagination] Set Page Size',
  props<{ size: number }>()
);

export const setSort = createAction(
  '[Pagination] Set Sort',
  props<{ sortField: string; sortDirection: string }>()
);

export const loadPagination = createAction(
  '[Pagination] Load Pagination'
);

export const loadPaginationSuccess = createAction(
  '[Pagination] Load Pagination Success',
  props<{ page: number; size: number; sortField: string; sortDirection: string }>()
);

export const loadPaginationFailure = createAction(
  '[Pagination] Load Pagination Failure',
  props<{ error: any }>()
);
