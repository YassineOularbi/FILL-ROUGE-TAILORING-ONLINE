import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map } from 'rxjs/operators';
import * as PaginationActions from '../actions/pagination.actions';

@Injectable()
export class PaginationEffects {
  private actions$ = inject(Actions);
  
  initPagination$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PaginationActions.initPagination),
      map(() => PaginationActions.setPaginationDefaults())
    )
  );
}
