import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { CepResponse } from '../models/viacep.model';

@Injectable({
  providedIn: 'root'
})
export class CepService {
  VIA_CEP_API_URL = environment.VIA_CEP_API;

  constructor(private http: HttpClient) { }

  searchCep(cep: string) {
    return this.http.get<CepResponse>(this.VIA_CEP_API_URL + cep + '/json')
    .pipe(
      map((response) => {
        return response
      })
  )

  }
}
