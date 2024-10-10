import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../interfaces/customer.interface';
import { environment } from '../../../environments/environment';
import { CustomerDto } from '../dtos/customer.dto';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = environment.apiUserManagement;

  constructor(private http: HttpClient) { }

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}api/customer/get-all-customers`);
  }

  getCustomerById(id: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}api/customer/get-customer-by-id/${id}`);
  }

  addCustomer(customerDto: CustomerDto): Observable<Customer> {
    return this.http.post<Customer>(`${this.apiUrl}api/customer/register`, customerDto);
  }

  updateCustomer(id: string, customerDto: CustomerDto): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}api/customer/update-customer/${id}`, customerDto);
  }

  deleteCustomer(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/customer/delete-customer/${id}`);
  }
}
