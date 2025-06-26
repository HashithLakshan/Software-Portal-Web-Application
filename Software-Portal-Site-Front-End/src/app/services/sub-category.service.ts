import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SubCategoryService {

  constructor(private http: HttpClient) { }
      private apiUrl = 'http://localhost:8081/api/subCategory';
 
     getAllSubCategories(commonStatus:any):Observable<any>{
         return this.http.get<any>(`${this.apiUrl}/getAllSub/${commonStatus}`);
    }
            }