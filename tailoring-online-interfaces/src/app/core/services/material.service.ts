import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Material } from '../interfaces/material.interface';
import { environment } from '../../../environments/environment';
import { MaterialDto } from '../dtos/material.dto';

@Injectable({
  providedIn: 'root'
})
export class MaterialService {
  private apiUrl = environment.apiStoreManagement;

  constructor(private http: HttpClient) { }

  getAllMaterials(page: number, size: number, sortField: string, sortDirection: string): Observable<Material[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<Material[]>(`${this.apiUrl}api/material/get-all-materials`, { params });
  }

  getAllMaterialsByStore(id: string, page: number, size: number, sortField: string, sortDirection: string): Observable<Material[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<Material[]>(`${this.apiUrl}api/material/get-all-materials-by-store/${id}`, { params });
  }

  getMaterialById(id: number): Observable<Material> {
    return this.http.get<Material>(`${this.apiUrl}api/material/get-material-by-id/${id}`);
  }

  addMaterial(materialDto: MaterialDto, storeId: number): Observable<Material> {
    return this.http.post<Material>(`${this.apiUrl}api/material/add-material/${storeId}`, materialDto);
  }

  updateMaterial(id: number, materialDto: MaterialDto): Observable<Material> {
    return this.http.put<Material>(`${this.apiUrl}api/material/update-material/${id}`, materialDto);
  }

  deleteMaterial(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/material/delete-material/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/material/search`, { params });
  }

  filter(page: number, size: number, sortField: string, sortDirection: string, typeFilter?: string, storeIdFilter?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (typeFilter) {
      params = params.set('typeFilter', typeFilter);
    }
    if (storeIdFilter) {
      params = params.set('storeIdFilter', storeIdFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/material/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/material/autocomplete`, { params });
  }
}
