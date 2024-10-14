import { createAction, props } from '@ngrx/store';

export const initPagination = createAction('[Pagination] Init Pagination');
export const setPaginationDefaults = createAction('[Pagination] Set Pagination Defaults');
export const setPage = createAction('[Pagination] Set Page', props<{ page: number }>());
export const setPageSize = createAction('[Pagination] Set Page Size', props<{ size: number }>());
export const setSort = createAction(
  '[Pagination] Set Sort',
  props<{ sortField: string; sortDirection: string }>()
);
export const setTotalRecords = createAction(
  '[Pagination] Set Total Records',
  props<{ totalRecords: number }>()
);
