import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";
import { AuthRequest } from "../interfaces/auth-request.interface";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
  })
  
  export class AuthService {

    constructor(private http: HttpClient) {}

    private apiUrl = `${environment.apiUser}api/`;

    signing(authRequest: AuthRequest): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}auth/login`, authRequest);
    }
  }