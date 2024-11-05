import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MaterialOption } from '../interfaces/material-option.interface';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MaterialOptionService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllMaterialOptions(page: number, size: number, sortField: string, sortDirection: string): Observable<MaterialOption[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<MaterialOption[]>(`${this.apiUrl}api/material-option/get-all-material-option`, { params });
  }

  getAllMaterialOptionsByCustomizableOption(id: string): Observable<MaterialOption[]> {
    return this.http.get<MaterialOption[]>(`${this.apiUrl}api/material-option/get-all-material-option-by-customizable-option/${id}`);
  }

  getMaterialOptionById(materialId: string, optionId: string): Observable<MaterialOption> {
    return this.http.get<MaterialOption>(`${this.apiUrl}api/material-option/get-material-option-by-id/${materialId}&${optionId}`);
  }

  addMaterialOption(materialId: string, optionId: string): Observable<MaterialOption> {
    return this.http.post<MaterialOption>(`${this.apiUrl}api/material-option/add-material-option/${materialId}&${optionId}`, null);
  }

  deleteMaterialOption(materialId: string, optionId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/material-option/delete-material-option/${materialId}&${optionId}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/material-option/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, materialTypeFilter?: string, optionIdFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (materialTypeFilter) {
      params = params.set('materialTypeFilter', materialTypeFilter);
    }
    if (optionIdFilter) {
      params = params.set('optionIdFilter', optionIdFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/material-option/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/material-option/autocomplete`, { params });
  }
}
