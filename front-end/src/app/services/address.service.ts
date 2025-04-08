import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Address } from '../models/contact.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getPFAddresses(contactId: number): Observable<Address[]> {
    return this.http.get<Address[]>(`${this.apiUrl}/endereco/pf/${contactId}`);
  }

  addPFAddress(contactId: number, address: Partial<Address>): Observable<Address> {
    return this.http.post<Address>(`${this.apiUrl}/endereco/pf/${contactId}`, address);
  }
  getPJAddresses(contactId: number): Observable<Address[]> {
    return this.http.get<Address[]>(`${this.apiUrl}/endereco/pj/${contactId}`);
  }

  addPJAddress(contactId: number, address: Partial<Address>): Observable<Address> {
    return this.http.post<Address>(`${this.apiUrl}/endereco/pj/${contactId}`, address);
  }

  removePFAddress(contactId: number, addressId: number): Observable<{ mensagem: string }> {
    return this.http.delete<{ mensagem: string }>(`${this.apiUrl}/endereco/pf/${contactId}/${addressId}`);
  }
  removePJAddress(contactId: number, addressId: number): Observable<{ mensagem: string }> {
    return this.http.delete<{ mensagem: string }>(`${this.apiUrl}/endereco/pj/${contactId}/${addressId}`);
  }

  updateAddress(addressId: number, address: Partial<Address>): Observable<Address> {
    return this.http.put<Address>(`${this.apiUrl}/endereco/update/${addressId}`, address);
  }
} 