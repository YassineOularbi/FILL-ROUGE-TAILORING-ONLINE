import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Product } from '../../../core/interfaces/product.interface';
import { ProductService } from '../../../core/services/product.service';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { AppState } from '../../../core/stores/app.state';
import { selectPage, selectPageSize, selectSortDirection, selectSortField, selectTotalRecords } from '../../../core/stores/pagination/selectors/pagination.selectors';
import * as PaginationActions from '../../../core/stores/pagination/actions/pagination.actions';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [
    MatCardModule,
    CommonModule
  ],
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  products: Product[] = [];
  page = 0;
  size = 9;
  sortField = 'name';
  sortDirection = 'asc';
  totalPages = 0;
  paginationSubscription!: Subscription;

  constructor(
    private productService: ProductService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.paginationSubscription = this.store.select(selectPage).subscribe((page) => {
      this.page = page;
      this.loadProducts();
    });

    this.paginationSubscription.add(
      this.store.select(selectPageSize).subscribe((size) => {
        this.size = size;
        this.loadProducts();
      })
    );

    this.paginationSubscription.add(
      this.store.select(selectSortField).subscribe((sortField) => {
        this.sortField = sortField;
        this.loadProducts();
      })
    );

    this.paginationSubscription.add(
      this.store.select(selectSortDirection).subscribe((sortDirection) => {
        this.sortDirection = sortDirection;
        this.loadProducts();
      })
    );
  }

  loadProducts(): void {
    this.productService.getAllProducts(this.page, this.size, this.sortField, this.sortDirection).subscribe({
      next: (response: any) => {
        this.products = response.content;
        this.totalPages = response.totalPages;

        this.store.dispatch(PaginationActions.setTotalRecords({ totalRecords: response.totalElements }));
      }
    });
  }

  ngOnDestroy(): void {
    if (this.paginationSubscription) {
      this.paginationSubscription.unsubscribe();
    }
  }
}
