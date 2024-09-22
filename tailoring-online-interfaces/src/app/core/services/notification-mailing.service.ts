import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../environments/environment";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class NotificationMailing {

  apiUrl = environment.apiNotification;

  constructor(private http: HttpClient) { }

  sendContactForm(name: string, email: string, phone: string, message: string): Observable<any> {
    let params = new HttpParams()
      .set('name', name)
      .set('email', email)
      .set('phone', phone)
      .set('message', message);

    return this.http.get<any>(`${this.apiUrl}api/email/contact-us`, { params });
  }
}
