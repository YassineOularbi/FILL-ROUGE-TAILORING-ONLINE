import { Component, OnInit, Output, EventEmitter, ChangeDetectionStrategy, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule, PaginatorModule, TableModule],
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PaginationComponent implements OnInit {
  page: number = 1;
  size: number = 10;
  sortField: string = 'name';
  sortDirection: string = 'asc';

  @Output() pageChange = new EventEmitter<{ page: number; size: number }>();
  @Output() sortChange = new EventEmitter<{ sortField: string; sortDirection: string }>();

  @Input() totalRecords: number = 0;

  ngOnInit(): void {
    this.emitPagination();
    this.emitSorting();
  }

  onPageChange(event: any): void {
    this.page = event.page + 1;
    this.size = event.rows;
    this.emitPagination();
  }

  onSort(event: any): void {
    this.sortField = event.field;
    this.sortDirection = event.order === 1 ? 'asc' : 'desc';
    this.emitSorting();
  }

  private emitPagination(): void {
    this.pageChange.emit({ page: this.page, size: this.size });
  }

  private emitSorting(): void {
    this.sortChange.emit({ sortField: this.sortField, sortDirection: this.sortDirection });
  }
}
