import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../interfaces/product.interface';
import { environment } from '../../../environments/environment';
import { ProductDto } from '../dtos/product.dto';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllProducts(page: number, size: number, sortField: string, sortDirection: string): Observable<Product[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<Product[]>(`${this.apiUrl}api/product/get-all-products`, { params });
  }

  getAllProductsByStore(id: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}api/product/get-all-products-by-store/${id}`);
  }

  getProductById(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}api/product/get-product-by-id/${id}`);
  }

  addProduct(productDto: ProductDto, id: string): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}api/product/add-product/${id}`, productDto);
  }

  updateProduct(id: number, productDto: ProductDto): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}api/product/update-product/${id}`, productDto);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/product/delete-product/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/product/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, categoryFilter?: string, nameFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (categoryFilter) {
      params = params.set('categoryFilter', categoryFilter);
    }
    if (nameFilter) {
      params = params.set('nameFilter', nameFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/product/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/product/autocomplete`, { params });
  }
}
