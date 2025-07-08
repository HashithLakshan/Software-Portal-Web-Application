import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerToSystemFeedbackService {

 constructor(private http: HttpClient) { }
     private apiUrl = 'http://localhost:8081/api/cTsf';

            saveFeedback(payload:any):Observable<any>{
              return this.http.post<any>(`${this.apiUrl}/save/feedback`, payload);
            }
}
