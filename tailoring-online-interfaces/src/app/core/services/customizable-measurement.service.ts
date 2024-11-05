import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomizableMeasurement } from '../interfaces/customizable-measurement.interface';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomizableMeasurementService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllCustomizableMeasurement(page: number, size: number, sortField: string, sortDirection: string): Observable<CustomizableMeasurement[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<CustomizableMeasurement[]>(`${this.apiUrl}api/customizable-measurement/get-all-customizable-measurement`, { params });
  }

  getAllCustomizableMeasurementByThreeDModel(id: string, page: number, size: number, sortField: string, sortDirection: string): Observable<CustomizableMeasurement[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<CustomizableMeasurement[]>(`${this.apiUrl}api/customizable-measurement/get-all-customizable-measurement-by-three-d-model/${id}`, { params });
  }

  getCustomizableMeasurementById(threeDModelId: string, measurementId: string): Observable<CustomizableMeasurement> {
    return this.http.get<CustomizableMeasurement>(`${this.apiUrl}api/customizable-measurement/get-customizable-measurement-by-id/${threeDModelId}&${measurementId}`);
  }

  addCustomizableMeasurement(threeDModelId: string, measurementId: string): Observable<CustomizableMeasurement> {
    return this.http.post<CustomizableMeasurement>(`${this.apiUrl}api/customizable-measurement/add-customizable-measurement/${threeDModelId}&${measurementId}`, null);
  }

  deleteCustomizableMeasurement(threeDModelId: string, measurementId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/customizable-measurement/delete-customizable-measurement/${threeDModelId}&${measurementId}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/customizable-measurement/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, modelIdFilter?: string, measurementNameFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (modelIdFilter) {
      params = params.set('modelIdFilter', modelIdFilter);
    }
    if (measurementNameFilter) {
      params = params.set('measurementNameFilter', measurementNameFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/customizable-measurement/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/customizable-measurement/autocomplete`, { params });
  }
}
