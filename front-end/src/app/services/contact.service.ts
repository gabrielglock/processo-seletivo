import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contact } from '../models/contact.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Contact endpoints
  getContacts(type: string = 'todos', page: number = 0, size: number = 30): Observable<any> {
    let params = new HttpParams()
      .set('type', type)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/contacts`, { params });
  }

  createPFContact(contact: Partial<Contact>): Observable<Contact> {
    return this.http.post<Contact>(`${this.apiUrl}/newContact/pf`, contact);
  }

  createPJContact(contact: Partial<Contact>): Observable<Contact> {
    return this.http.post<Contact>(`${this.apiUrl}/newContact/pj`, contact);
  }

  updateContact(id: number, contact: Partial<Contact>): Observable<Contact> {
    const type = contact.type?.toLowerCase() || '';
    return this.http.put<Contact>(`${this.apiUrl}/updateContact/${type}/${id}`, contact);
  }

  deleteContact(id: number, type: string): Observable<any> {
    return type === 'PF' 
      ? this.http.delete<any>(`${this.apiUrl}/delContact/pf/${id}`) 
      : this.http.delete<any>(`${this.apiUrl}/delContact/pj/${id}`);
  }

  searchContacts(query: string): Observable<any> {
    const params = new HttpParams().set('query', query);
    return this.http.get<any>(`${this.apiUrl}/contacts/search`, { params });
  }
} 