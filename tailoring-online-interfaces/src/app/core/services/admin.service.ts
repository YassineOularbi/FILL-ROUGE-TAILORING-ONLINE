import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Admin } from '../interfaces/admin.interface';
import { environment } from '../../../environments/environment';
import { AdminDto } from '../dtos/admin.dto';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = environment.apiUserManagement;

  constructor(private http: HttpClient) { }

  getAllAdmins(): Observable<Admin[]> {
    return this.http.get<Admin[]>(`${this.apiUrl}api/admin/get-all-admins`);
  }

  getAdminById(id: string): Observable<Admin> {
    return this.http.get<Admin>(`${this.apiUrl}api/admin/get-admin-by-id/${id}`);
  }

  addAdmin(adminDto: AdminDto): Observable<Admin> {
    return this.http.post<Admin>(`${this.apiUrl}api/admin/register`, adminDto);
  }

  updateAdmin(id: string, adminDto: AdminDto): Observable<Admin> {
    return this.http.put<Admin>(`${this.apiUrl}api/admin/update-admin/${id}`, adminDto);
  }

  deleteAdmin(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/admin/delete-admin/${id}`);
  }
}
