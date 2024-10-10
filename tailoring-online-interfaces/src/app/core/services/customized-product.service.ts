import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomizedProduct } from '../interfaces/customized-product.interface';
import { environment } from '../../../environments/environment';
import { CustomizedProductDto } from '../dtos/customized-product.dto';

@Injectable({
  providedIn: 'root'
})
export class CustomizedProductService {
  private apiUrl = environment.apiOrderManagement;

  constructor(private http: HttpClient) { }

  getAllCustomizedProducts(page: number, size: number, sortField: string, sortDirection: string): Observable<CustomizedProduct[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<CustomizedProduct[]>(`${this.apiUrl}api/customized-product/get-all-customized-products`, { params });
  }

  getCustomizedProductById(id: string): Observable<CustomizedProduct> {
    return this.http.get<CustomizedProduct>(`${this.apiUrl}api/customized-product/get-customized-product-by-id/${id}`);
  }

  getCustomizedProductWithProduct(id: string): Observable<CustomizedProduct> {
    return this.http.get<CustomizedProduct>(`${this.apiUrl}api/customized-product/get-customized-product-with-product/${id}`);
  }

  addCustomizedProduct(customizedProductDto: CustomizedProductDto, productId: string): Observable<CustomizedProduct> {
    return this.http.post<CustomizedProduct>(`${this.apiUrl}api/customized-product/add-customized-product/${productId}`, customizedProductDto);
  }

  updateCustomizedProduct(id: string, customizedProductDto: CustomizedProductDto): Observable<CustomizedProduct> {
    return this.http.put<CustomizedProduct>(`${this.apiUrl}api/customized-product/update-customized-product/${id}`, customizedProductDto);
  }

  deleteCustomizedProduct(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/customized-product/delete-customized-product/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/customized-product/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, productIdFilter?: string, measurementFilter?: string, optionFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (productIdFilter) {
      params = params.set('productIdFilter', productIdFilter);
    }
    if (measurementFilter) {
      params = params.set('measurementFilter', measurementFilter);
    }
    if (optionFilter) {
      params = params.set('optionFilter', optionFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/customized-product/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/customized-product/autocomplete`, { params });
  }
}
