import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SystemProfileFeaturesService {

     constructor(private http: HttpClient) { }
        private apiUrl = 'http://localhost:8081/api/systemFeatures';
  

        getByIdSystemProfileId(systemProfilesId:any,commonStatus:any):Observable<any>{
          return this.http.get<any>(`${this.apiUrl}/getAllFeaturesinOneProfile/${systemProfilesId}/${commonStatus}`);
        }
        saveFeatures(payload: any): Observable<any> {
          return this.http.post<any>(`${this.apiUrl}/save`, payload);
        }

      deleteFeature(systemFeatureId:any): Observable<any> {
          const url = `${this.apiUrl}/delete?systemFeatureId=${systemFeatureId}`;
          return this.http.delete<any>(url, {}); // Added empty body to comply with PUT request standards
        }
}
