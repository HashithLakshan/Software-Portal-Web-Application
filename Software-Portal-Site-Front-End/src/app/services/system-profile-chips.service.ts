import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SystemProfileChipsService {

  constructor(private http: HttpClient) { }
         private apiUrl = 'http://localhost:8081/api/systemProfileChips';
   
         getByIdSystemProfileId(systemProfilesId:any,commonStatus:any):Observable<any>{
           return this.http.get<any>(`${this.apiUrl}/getAllOneProfileChips/${systemProfilesId}/${commonStatus}`);
         }

         saveChips(payload: any): Observable<any> {
          return this.http.post<any>(`${this.apiUrl}/save`, payload);
        }

        deleteChips(systemProfileChipId:any): Observable<any> {
          const url = `${this.apiUrl}/delete?systemProfileChipId=${systemProfileChipId}`;
          return this.http.delete<any>(url, {}); // Added empty body to comply with PUT request standards
        }
}
