import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Measurement } from '../interfaces/measurement.interface';
import { environment } from '../../../environments/environment';
import { MeasurementDto } from '../dtos/measurement.dto';

@Injectable({
  providedIn: 'root'
})
export class MeasurementService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllMeasurements(page: number, size: number, sortField: string, sortDirection: string): Observable<Measurement[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<Measurement[]>(`${this.apiUrl}api/measurement/get-all-measurements`, { params });
  }

  getMeasurementById(id: number): Observable<Measurement> {
    return this.http.get<Measurement>(`${this.apiUrl}api/measurement/get-measurement-by-id/${id}`);
  }

  addMeasurement(measurementDto: MeasurementDto): Observable<Measurement> {
    return this.http.post<Measurement>(`${this.apiUrl}api/measurement/add-measurement`, measurementDto);
  }

  updateMeasurement(id: number, measurementDto: MeasurementDto): Observable<Measurement> {
    return this.http.put<Measurement>(`${this.apiUrl}api/measurement/update-measurement/${id}`, measurementDto);
  }

  deleteMeasurement(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/measurement/delete-measurement/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/measurement/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, nameFilter?: string, descriptionFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (nameFilter) {
      params = params.set('nameFilter', nameFilter);
    }
    if (descriptionFilter) {
      params = params.set('descriptionFilter', descriptionFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/measurement/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/measurement/autocomplete`, { params });
  }
}
