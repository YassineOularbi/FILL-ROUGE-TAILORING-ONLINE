import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class AuthService {

    constructor(private http: HttpClient) { }

    signin(username: string, password: string): Observable<any> {
        const body = new HttpParams()
            .set('username', username)
            .set('password', password)
            .set('grant_type', 'password')
            .set('client_id', 'tailoring-online-id');

        return this.http.post('http://localhost:8080/realms/tailoring-online/protocol/openid-connect/token', body.toString(), {
            headers: new HttpHeaders()
                .set('Content-Type', 'application/x-www-form-urlencoded')
        });
    }
}