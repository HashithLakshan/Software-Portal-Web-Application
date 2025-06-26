import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

   constructor(private http: HttpClient) { }
       private apiUrl = 'http://localhost:8081/';
     
       saveUser(payload: any): Observable<any> {
         return this.http.post<any>(`${this.apiUrl}product/v1/checkout`, payload);
       }
}
