import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class TimeSlotServiceService {

 constructor(private http: HttpClient) { }
      private apiUrl = 'http://localhost:8081/api/timeSlots';

        

getAllTimeSlots(commonStatus:any):Observable<any>{
              return this.http.get<any>(`${this.apiUrl}/getAll/${commonStatus}`);
            }

 getAllTimeSlotActive(commonStatus: string, page: number, size: number): Observable<any> {
              return this.http.get<any>(`${this.apiUrl}/filtered?commonStatus=${commonStatus}&page=${page}&size=${size}`);
            }

  saveTimeSlot(payload: any): Observable<any> {
  return this.http.post(`${this.apiUrl}/save`, payload);
}


            statusUpdate(zoomTimeSlotId:string,commonStatus: string): Observable<any> {
              const url = `${this.apiUrl}/delete?zoomTimeSlotId=${zoomTimeSlotId}&commonStatus=${commonStatus}`;
              return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
            }
          
}