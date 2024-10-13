import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as PaginationActions from './pagination.actions';
import { mergeMap, map, catchError, withLatestFrom, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { Store } from '@ngrx/store';
import { PaginationState } from './pagination.reducer';
import { selectPaginationState } from './pagination.selectors';

@Injectable()
export class PaginationEffects {
  loadPagination$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PaginationActions.loadPagination),
      mergeMap(() =>
        of(localStorage.getItem('pagination')).pipe(
          map(data => {
            if (data) {
              const parsed = JSON.parse(data);
              return PaginationActions.loadPaginationSuccess({
                page: parsed.page,
                size: parsed.size,
                sortField: parsed.sortField,
                sortDirection: parsed.sortDirection
              });
            } else {
              return PaginationActions.loadPaginationSuccess({
                page: 1,
                size: 10,
                sortField: 'name',
                sortDirection: 'asc'
              });
            }
          }),
          catchError(error => of(PaginationActions.loadPaginationFailure({ error })))
        )
      )
    )
  );

  savePagination$ = createEffect(() =>
    this.actions$.pipe(
      ofType(
        PaginationActions.setPage,
        PaginationActions.setPageSize,
        PaginationActions.setSort
      ),
      withLatestFrom(this.store.select(selectPaginationState)),
      tap(([action, state]) => {
        localStorage.setItem('pagination', JSON.stringify(state));
      })
    ),
    { dispatch: false }
  );

  constructor(private actions$: Actions, private store: Store<PaginationState>) {}
}
