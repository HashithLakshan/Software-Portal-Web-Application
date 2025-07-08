import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SystemProfilePhotosService {


  private baseUrl = 'http://localhost:8081/api/SystemProfileImages';  // Adjust to your API base URL

  constructor(private http: HttpClient) {}

  getImages(systemProfilesId: any): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/${systemProfilesId}`);
  }

  uploadImgs(payload: any): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('image', payload.image);
    formData.append('systemProfileId', payload.systemProfileId);
    return this.http.post<any>(`${this.baseUrl}/save`, formData);
  }

 

  getImagesAll(systemProfilesId: any): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/getAllPhoto?systemProfilesId=${systemProfilesId}`);
  }

  getImagesallaaaaa(systemProfilesId: any): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/ll/${systemProfilesId}`);
  }

  uploadImgsUpdate(payload: any): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('image', payload.image);
    formData.append('id', payload.id);
    return this.http.put<any>(`${this.baseUrl}/update`, formData);
  }

  deleteImage(id:string): Observable<any> {
    const url = `${this.baseUrl}/delete?id=${id}`;
    return this.http.delete<any>(url, {}); // Added empty body to comply with PUT request standards
  }
}
