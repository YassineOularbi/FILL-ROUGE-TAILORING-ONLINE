import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Store } from '../interfaces/store.interface';
import { environment } from '../../../environments/environment';
import { StoreDto } from '../dtos/store.dto';

@Injectable({
  providedIn: 'root'
})
export class StoreService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllStores(page: number, size: number, sortField: string, sortDirection: string): Observable<Store[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<Store[]>(`${this.apiUrl}api/store/get-all-stores`, { params });
  }

  getStoreById(id: string): Observable<Store> {
    return this.http.get<Store>(`${this.apiUrl}api/store/get-store-by-id/${id}`);
  }

  getStoreWithDetails(id: string): Observable<Store> {
    return this.http.get<Store>(`${this.apiUrl}api/store/get-store-with-tailor/${id}`);
  }

  addStore(storeDto: StoreDto, id: string): Observable<Store> {
    return this.http.post<Store>(`${this.apiUrl}api/store/add-store/${id}`, storeDto);
  }

  updateStore(id: number, storeDto: StoreDto): Observable<Store> {
    return this.http.put<Store>(`${this.apiUrl}api/store/update-store/${id}`, storeDto);
  }

  deleteStore(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/store/delete-store/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/store/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, nameFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (nameFilter) {
      params = params.set('nameFilter', nameFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/store/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/store/autocomplete`, { params });
  }
}
