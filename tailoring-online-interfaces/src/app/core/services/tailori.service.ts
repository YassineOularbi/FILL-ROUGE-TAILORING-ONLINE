import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tailor } from '../interfaces/tailor.interface';
import { environment } from '../../../environments/environment';
import { TailorDto } from '../dtos/tailor.dto';

@Injectable({
  providedIn: 'root'
})
export class TailorService {
  private apiUrl = environment.apiUserManagement;

  constructor(private http: HttpClient) { }

  getAllTailors(): Observable<Tailor[]> {
    return this.http.get<Tailor[]>(`${this.apiUrl}api/tailor/get-all-tailors`);
  }

  getTailorById(id: string): Observable<Tailor> {
    return this.http.get<Tailor>(`${this.apiUrl}api/tailor/get-tailor-by-id/${id}`);
  }

  addTailor(tailorDto: TailorDto): Observable<Tailor> {
    return this.http.post<Tailor>(`${this.apiUrl}api/tailor/register`, tailorDto);
  }

  updateTailor(id: string, tailorDto: TailorDto): Observable<Tailor> {
    return this.http.put<Tailor>(`${this.apiUrl}api/tailor/update-tailor/${id}`, tailorDto);
  }

  deleteTailor(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/tailor/delete-tailor/${id}`);
  }
}
