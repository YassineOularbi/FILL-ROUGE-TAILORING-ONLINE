import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AppState } from '../../../core/stores/app.state';
import { selectPage, selectPageSize, selectSortDirection, selectSortField } from '../../../core/stores/pagination/selectors/pagination.selectors';
import * as PaginationActions from '../../../core/stores/pagination/actions/pagination.actions';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule, PaginatorModule, TableModule],
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PaginationComponent implements OnInit {
  page$?: Observable<number>;
  size$?: Observable<number>;
  sortField$: Observable<string>;
  sortDirection$: Observable<string>;

  @Input() totalRecords: number = 0;

  constructor(private store: Store<AppState>) {
    this.page$ = this.store.select(selectPage);
    this.size$ = this.store.select(selectPageSize);
    this.sortField$ = this.store.select(selectSortField);
    this.sortDirection$ = this.store.select(selectSortDirection);
  }

  ngOnInit(): void {
    this.store.dispatch(PaginationActions.initPagination());
  }

  onPageChange(event: any): void {
    this.store.dispatch(PaginationActions.setPage({ page: event.page + 1 }));
    this.store.dispatch(PaginationActions.setPageSize({ size: event.rows }));
  }

  onSort(event: any): void {
    this.store.dispatch(PaginationActions.setSort({ sortField: event.field, sortDirection: event.order === 1 ? 'asc' : 'desc' }));
  }
}
