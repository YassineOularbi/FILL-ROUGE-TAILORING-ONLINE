import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ThreeDModel } from '../interfaces/three-d-model.interface';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ThreeDModelService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllThreeDModel(page: number, size: number, sortField: string, sortDirection: string): Observable<ThreeDModel[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<ThreeDModel[]>(`${this.apiUrl}api/three-d-model/get-all-three-d-model`, { params });
  }

  getThreeDModelByProduct(id: string): Observable<ThreeDModel> {
    return this.http.get<ThreeDModel>(`${this.apiUrl}api/three-d-model/get-three-d-model-by-product/${id}`);
  }

  getThreeDModelById(id: string): Observable<ThreeDModel> {
    return this.http.get<ThreeDModel>(`${this.apiUrl}api/three-d-model/get-three-d-model-by-id/${id}`);
  }

  addThreeDModel(id: string): Observable<ThreeDModel> {
    return this.http.post<ThreeDModel>(`${this.apiUrl}api/three-d-model/add-three-d-model/${id}`, null);
  }

  deleteThreeDModel(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/three-d-model/delete-three-d-model/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/three-d-model/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, productIdFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (productIdFilter) {
      params = params.set('productIdFilter', productIdFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/three-d-model/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/three-d-model/autocomplete`, { params });
  }
}
