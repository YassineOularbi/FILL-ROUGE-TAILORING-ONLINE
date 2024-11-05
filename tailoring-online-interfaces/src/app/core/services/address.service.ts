import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Address } from '../interfaces/address.interface';
import { environment } from '../../../environments/environment';
import { AddressDto } from '../dtos/address.dto';

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private apiUrl = environment.apiLocalizationShipping;

  constructor(private http: HttpClient) { }

  getAllAddresses(page: number, size: number, sortField: string, sortDirection: string): Observable<Address[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<Address[]>(`${this.apiUrl}api/address/get-all-addresses`, { params });
  }

  getAddressById(id: string): Observable<Address> {
    return this.http.get<Address>(`${this.apiUrl}api/address/get-address-by-id/${id}`);
  }

  getAddressWithUser(id: string): Observable<Address> {
    return this.http.get<Address>(`${this.apiUrl}api/address/get-address-with-user/${id}`);
  }

  addAddress(addressDto: AddressDto, id: string): Observable<Address> {
    return this.http.post<Address>(`${this.apiUrl}api/address/add-address/${id}`, addressDto);
  }

  updateAddress(id: string, addressDto: AddressDto): Observable<Address> {
    return this.http.put<Address>(`${this.apiUrl}api/address/update-address/${id}`, addressDto);
  }

  deleteAddress(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}api/address/delete-address/${id}`);
  }

  search(input: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('input', input)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    return this.http.get<any>(`${this.apiUrl}api/address/search`, { params });
  }

  filter(
    page: number,
    size: number,
    sortField: string,
    sortDirection: string,
    addressFilter?: string,
    suiteFilter?: string,
    cityFilter?: string,
    provinceFilter?: string,
    countryFilter?: string,
    defaultFilter?: boolean,
    zipCodeFilter?: string
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);
    if (addressFilter) {
      params = params.set('addressFilter', addressFilter);
    }
    if (suiteFilter) {
      params = params.set('suiteFilter', suiteFilter);
    }
    if (cityFilter) {
      params = params.set('cityFilter', cityFilter);
    }
    if (provinceFilter) {
      params = params.set('provinceFilter', provinceFilter);
    }
    if (countryFilter) {
      params = params.set('countryFilter', countryFilter);
    }
    if (defaultFilter !== undefined && defaultFilter !== null) {
      params = params.set('defaultFilter', defaultFilter.toString());
    }
    if (zipCodeFilter) {
      params = params.set('zipCodeFilter', zipCodeFilter);
    }
    return this.http.get<any>(`${this.apiUrl}api/address/filter`, { params });
  }

  autocomplete(input: string): Observable<any> {
    let params = new HttpParams().set('input', input);
    return this.http.get<any>(`${this.apiUrl}api/address/autocomplete`, { params });
  }
}
