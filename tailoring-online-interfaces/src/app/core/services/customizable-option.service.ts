import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomizableOption } from '../interfaces/customizable-option.interface';
import { environment } from '../../../environments/environment';
import { MaterialType } from '../enums/material-type.enum';

@Injectable({
  providedIn: 'root'
})
export class CustomizableOptionService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllCustomizableOptions(page: number, size: number, sortField: string, sortDirection: string): Observable<CustomizableOption[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<CustomizableOption[]>(`${this.apiUrl}api/customizable-option/get-all-customizable-option`, { params });
  }

  getAllCustomizableOptionsByThreeDModel(id: string): Observable<CustomizableOption[]> {
    return this.http.get<CustomizableOption[]>(`${this.apiUrl}api/customizable-option/get-all-customizable-option-by-three-d-model/${id}`);
  }

  getCustomizableOptionById(id: string): Observable<CustomizableOption> {
    return this.http.get<CustomizableOption>(`${this.apiUrl}api/customizable-option/get-customizable-option-by-id/${id}`);
  }

  addCustomizableOption(id: string, type: MaterialType): Observable<CustomizableOption> {
    return this.http.post<CustomizableOption>(`${this.apiUrl}api/customizable-option/add-customizable-option/${id}&${type}`, null);
  }

  deleteCustomizableOption(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/customizable-option/delete-customizable-option/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/customizable-option/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, modelIdFilter?: string, typeFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (modelIdFilter) {
      params = params.set('modelIdFilter', modelIdFilter);
    }
    if (typeFilter) {
      params = params.set('typeFilter', typeFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/customizable-option/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/customizable-option/autocomplete`, { params });
  }
}
