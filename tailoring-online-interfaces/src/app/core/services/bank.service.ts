import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bank } from '../interfaces/bank.interface';
import { environment } from '../../../environments/environment';
import { BankDto } from '../dtos/bank.dto';

@Injectable({
  providedIn: 'root'
})
export class BankService {
  private apiUrl = environment.apiPaymentBanking;

  constructor(private http: HttpClient) { }

  getAllBanks(page: number, size: number, sortField: string, sortDirection: string): Observable<Bank[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<Bank[]>(`${this.apiUrl}api/bank/get-all-banks`, { params });
  }

  getBankById(id: number): Observable<Bank> {
    return this.http.get<Bank>(`${this.apiUrl}api/bank/get-bank-by-id/${id}`);
  }

  getBankWithUser(id: number): Observable<Bank> {
    return this.http.get<Bank>(`${this.apiUrl}api/bank/get-bank-with-user/${id}`);
  }

  addBank(bankDto: BankDto, id: string): Observable<Bank> {
    return this.http.post<Bank>(`${this.apiUrl}api/bank/add-bank/${id}`, bankDto);
  }

  updateBank(id: number, bankDto: BankDto): Observable<Bank> {
    return this.http.put<Bank>(`${this.apiUrl}api/bank/update-bank/${id}`, bankDto);
  }

  deleteBank(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/bank/delete-bank/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/bank/search`, { params });
  }

  filter(
    page: number,
    size: number,
    sortField: string,
    sortDirection: string,
    cardNumberFilter?: string,
    expirationDateFrom?: string,
    expirationDateTo?: string,
    cvcFilter?: string,
    userIdFilter?: string
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (cardNumberFilter) {
      params = params.set('cardNumberFilter', cardNumberFilter);
    }
    if (expirationDateFrom) {
      params = params.set('expirationDateFrom', expirationDateFrom);
    }
    if (expirationDateTo) {
      params = params.set('expirationDateTo', expirationDateTo);
    }
    if (cvcFilter) {
      params = params.set('cvcFilter', cvcFilter);
    }
    if (userIdFilter) {
      params = params.set('userIdFilter', userIdFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/bank/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/bank/autocomplete`, { params });
  }
}
