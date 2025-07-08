import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) { }

  private apiUrl = 'http://localhost:8081/image/';
  

 
  uploadImgs(payload: any): Observable<any> {
    console.log(payload)
    const formData: FormData = new FormData();
    formData.append('image', payload.image);
    formData.append('employeeId', payload.userId);
    console.log(formData);
    return this.http.post<any>(`${this.apiUrl}save`, formData);
  }
  
  getUserImage(employeeId: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}${employeeId}`, { responseType: 'blob' });
  }
  

}
