import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomizedOption } from '../interfaces/customized-option.interface';
import { environment } from '../../../environments/environment';
import { CustomizedOptionDto } from '../dtos/customized-option.dto';

@Injectable({
  providedIn: 'root'
})
export class CustomizedOptionService {
  private apiUrl = environment.apiOrderManagement;

  constructor(private http: HttpClient) { }

  getAllCustomizedOptions(page: number, size: number, sortField: string, sortDirection: string): Observable<CustomizedOption[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<CustomizedOption[]>(`${this.apiUrl}api/customized-option/get-all-customized-options`, { params });
  }

  getCustomizedOptionById(id: string): Observable<CustomizedOption> {
    return this.http.get<CustomizedOption>(`${this.apiUrl}api/customized-option/get-customized-option-by-id/${id}`);
  }

  getCustomizedOptionWithDetails(id: string): Observable<CustomizedOption> {
    return this.http.get<CustomizedOption>(`${this.apiUrl}api/customized-option/get-customized-option-with-details/${id}`);
  }

  addCustomizedOption(customizedOptionDto: CustomizedOptionDto, productId: string, materialId: string): Observable<CustomizedOption> {
    return this.http.post<CustomizedOption>(`${this.apiUrl}api/customized-option/add-customized-option/${productId}&${materialId}`, customizedOptionDto);
  }

  updateCustomizedOption(id: string, customizedOptionDto: CustomizedOptionDto): Observable<CustomizedOption> {
    return this.http.put<CustomizedOption>(`${this.apiUrl}api/customized-option/update-customized-option/${id}`, customizedOptionDto);
  }

  deleteCustomizedOption(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/customized-option/delete-customized-option/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/customized-option/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, typeFilter?: string, materialIdFilter?: string, productIdFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (typeFilter) {
      params = params.set('typeFilter', typeFilter);
    }
    if (materialIdFilter) {
      params = params.set('materialIdFilter', materialIdFilter);
    }
    if (productIdFilter) {
      params = params.set('productIdFilter', productIdFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/customized-option/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/customized-option/autocomplete`, { params });
  }
}
