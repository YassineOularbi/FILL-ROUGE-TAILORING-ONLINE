import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../environments/environment";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class MailingService {

  apiUrl = environment.apiNotificationMailing;

  constructor(private http: HttpClient) { }

  sendContactForm(name: string, email: string, phone: string, message: string): Observable<string> {
    let params = new HttpParams()
      .set('name', name)
      .set('email', email)
      .set('phone', phone)
      .set('message', message);

    return this.http.get<string>(`${this.apiUrl}api/email/contact-us`, { params, responseType: 'text' as 'json' });
  }

  sendVerificationCode(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}api/email/send-verification-code/${encodeURIComponent(email)}`);
  }

  sendOTPVerification(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}api/email/send-otp-verification/${encodeURIComponent(email)}`);
  }

  verifyCode(email: string, code: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}api/email/verify-code/${encodeURIComponent(email)}&${encodeURIComponent(code)}`);
  }
}
