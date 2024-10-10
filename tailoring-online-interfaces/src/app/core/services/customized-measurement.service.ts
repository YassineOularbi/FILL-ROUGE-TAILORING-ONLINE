import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomizedMeasurement } from '../interfaces/customized-measurement.interface';
import { environment } from '../../../environments/environment';
import { CustomizedMeasurementDto } from '../dtos/customized-measurement.dto';

@Injectable({
  providedIn: 'root'
})
export class CustomizedMeasurementService {
  private apiUrl = environment.apiOrderManagement;

  constructor(private http: HttpClient) { }

  getAllCustomizedMeasurements(page: number, size: number, sortField: string, sortDirection: string): Observable<CustomizedMeasurement[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<CustomizedMeasurement[]>(`${this.apiUrl}api/customized-measurement/get-all-customized-measurements`, { params });
  }

  getCustomizedMeasurementById(id: string): Observable<CustomizedMeasurement> {
    return this.http.get<CustomizedMeasurement>(`${this.apiUrl}api/customized-measurement/get-customized-measurement-by-id/${id}`);
  }

  getCustomizedMeasurementWithDetails(id: string): Observable<CustomizedMeasurement> {
    return this.http.get<CustomizedMeasurement>(`${this.apiUrl}api/customized-measurement/get-customized-measurement-with-details/${id}`);
  }

  addCustomizedMeasurement(customizedMeasurementDto: CustomizedMeasurementDto, measurementId: string, customizedProductId: string): Observable<CustomizedMeasurement> {
    return this.http.post<CustomizedMeasurement>(`${this.apiUrl}api/customized-measurement/add-customized-measurement/${measurementId}&${customizedProductId}`, customizedMeasurementDto);
  }

  updateCustomizedMeasurement(id: string, customizedMeasurementDto: CustomizedMeasurementDto): Observable<CustomizedMeasurement> {
    return this.http.put<CustomizedMeasurement>(`${this.apiUrl}api/customized-measurement/update-customized-measurement/${id}`, customizedMeasurementDto);
  }

  deleteCustomizedMeasurement(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/customized-measurement/delete-customized-measurement/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/customized-measurement/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, measurementIdFilter?: string, valueFilter?: number, unitFilter?: string, productIdFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (measurementIdFilter) {
      params = params.set('measurementIdFilter', measurementIdFilter);
    }
    if (valueFilter !== undefined && valueFilter !== null) {
      params = params.set('valueFilter', valueFilter.toString());
    }
    if (unitFilter) {
      params = params.set('unitFilter', unitFilter);
    }
    if (productIdFilter) {
      params = params.set('productIdFilter', productIdFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/customized-measurement/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/customized-measurement/autocomplete`, { params });
  }
}
