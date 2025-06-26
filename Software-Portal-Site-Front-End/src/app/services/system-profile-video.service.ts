import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SystemProfileVideoService {

 
    private apiUrl = 'http://localhost:8081/api/systemProfileVideo'; // Replace with your backend endpoint
  
    constructor(private http: HttpClient) {}
  
    getVideo(systemProfileId: any): Observable<Blob> {
      return this.http.get(`${this.apiUrl}/${systemProfileId}`, {
        responseType: 'blob', 
      });
    }

    uploadVideo(file: File, systemProfileId: string): Observable<any> {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('systemProfileId', systemProfileId);
      return this.http.post<any>(`${this.apiUrl}/upload`, formData);
    }
  }


