import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Product } from '../../../core/interfaces/product.interface';
import { ProductService } from '../../../core/services/product.service';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [
    MatCardModule
  ],
  templateUrl: './product.component.html',
  styleUrl: './product.component.scss'
})
export class ProductComponent implements OnInit {
  products: Product[] = [];
  page = 0;
  size = 9;
  sortField = 'name';
  sortDirection = 'asc';
  totalPages = 0;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAllProducts(this.page, this.size, this.sortField, this.sortDirection).subscribe({
      next: (response: any) => {
        this.products = response.content;
        this.totalPages = response.totalPages;
      }
    });
  }

  onPageChange(newPage: number): void {
    this.page = newPage;
    this.loadProducts();
  }
}
