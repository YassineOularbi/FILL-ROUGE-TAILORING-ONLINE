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

    private apiUrl = environment.apiUserManagement;

    signing(authRequest: AuthRequest): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}api/auth/login`, authRequest);
    }

    sendVerificationCode(id: string): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}api/auth/send-verification-code/${id}`, null);
      }
    
      verifyEmail(id: string, code: string): Observable<string> {
        return this.http.post<string>(`${this.apiUrl}api/auth/verify-email/${id}&${code}`, null);
      }
    
      sendOTPByEmail(id: string): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}api/auth/send-otp-verification/${id}`, null);
      }
    
      verifyOtpCode(id: string, code: string): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}api/auth/verify-otp/${id}&${code}`, null);
      }
  }