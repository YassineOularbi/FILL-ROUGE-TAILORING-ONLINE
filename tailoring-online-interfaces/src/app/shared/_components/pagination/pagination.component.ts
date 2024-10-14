import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { AppState } from '../../../core/stores/app.state';
import { selectPage, selectPageSize, selectSortDirection, selectSortField, selectTotalRecords } from '../../../core/stores/pagination/selectors/pagination.selectors';
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
  totalRecords$?: Observable<number>;
  sortField$: Observable<string>;
  sortDirection$: Observable<string>;

  constructor(private store: Store<AppState>) {
    this.page$ = this.store.select(selectPage);
    this.size$ = this.store.select(selectPageSize);
    this.sortField$ = this.store.select(selectSortField);
    this.sortDirection$ = this.store.select(selectSortDirection);
    this.totalRecords$ = this.store.select(selectTotalRecords);
  }

  ngOnInit(): void {
    this.store.dispatch(PaginationActions.initPagination());
  }

  onPageChange(event: any): void {
    console.log(event);
  
    this.store.dispatch(PaginationActions.setPage({ page: event.page }));
    this.store.dispatch(PaginationActions.setPageSize({ size: event.rows }));
  }
  

  onSort(event: any): void {
    this.store.dispatch(PaginationActions.setSort({ sortField: event.field, sortDirection: event.order === 1 ? 'asc' : 'desc' }));
  }
}
